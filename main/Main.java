package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.banking.*;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.*;
import org.poo.transactions.PrintTransactions;
import org.poo.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    //initializez instantele statice
    private static final PrintUsers PRINT_USERS = new PrintUsers();
    private static final AddAccount ADD_ACCOUNT = new AddAccount();
    private static final CreateCard CREATE_CARD = new CreateCard();
    private static final AddFunds ADD_FUNDS = new AddFunds();
    private static final DeleteAccount DELETE_ACCOUNT = new DeleteAccount();
    private static final CreateOneTimeCard CREATE_ONE_TIME_CARD = new CreateOneTimeCard();
    private static final PayOnline PAY_ONLINE = new PayOnline();
    private static final DeleteCard DELETE_CARD = new DeleteCard();
    private static final SendMoney SEND_MONEY = new SendMoney();
    private static final PrintTransactions PRINT_TRANSACTIONS = new PrintTransactions();
    private static final SplitPayment SPLIT_PAYMENT = new SplitPayment();
    private static final Report REPORT = new Report();
    private static final MinimumBalance MINIMUM_BALANCE = new MinimumBalance();
    private static final CardStatus CARD_STATUS = new CardStatus();
    private static final WithdrawSavings WITHDRAW_SAVINGS = new WithdrawSavings();
    private static final UpgradePlan UPGRADE_PLAN = new UpgradePlan();
    private static final CashWithdrawal CASH_WITHDRAWAL = new CashWithdrawal();
    private static final AddInterest ADD_INTEREST = new AddInterest();

    /**
     *
     * @param filePath1
     * @param filePath2
     * @throws IOException
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        Utils.resetRandom();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);
        ArrayNode output = objectMapper.createArrayNode();
        List<User> users = new ArrayList<>();

        //initializez ratele
        if (inputData.getExchangeRates() != null) {
            Converter.getInstance().initializare(inputData.getExchangeRates());
        }

        //cream utilizatorii
        for (UserInput userInput : inputData.getUsers()) {
            users.add(new User(userInput.getFirstName(),
                    userInput.getLastName(),
                    userInput.getEmail(),
                    userInput.getBirthDate(),
                    userInput.getOccupation()));
        }

        //metodele pentru comenzile noastre
        for (CommandInput cmd : inputData.getCommands()) {
            switch (cmd.getCommand()) {
                case "printUsers":
                    PRINT_USERS.handlePrintUsers(objectMapper, output, users, cmd.getTimestamp());
                    break;
                case "addAccount":
                    if (cmd.getAccountType().equals("savings")) {
                        ADD_ACCOUNT.handleAddAccount(users, cmd.getEmail(), cmd.getCurrency(),
                                cmd.getAccountType(), cmd.getTimestamp(), cmd.getInterestRate());
                    } else {
                        ADD_ACCOUNT.handleAddAccount(users, cmd.getEmail(), cmd.getCurrency(),
                                cmd.getAccountType(), cmd.getTimestamp(), null);
                    }
                    break;
                case "createCard":
                    CREATE_CARD.handleCreateCard(users, cmd.getEmail(), cmd.getAccount(),
                            cmd.getTimestamp());
                    break;
                case "addFunds":
                    ADD_FUNDS.handleAddFunds(users, cmd.getAccount(), cmd.getAmount());
                    break;
                case "deleteAccount":
                    DELETE_ACCOUNT.handleDeleteAccount(objectMapper, output, users, cmd.getEmail(),
                            cmd.getAccount(), cmd.getTimestamp());
                    break;
                case "createOneTimeCard":
                    CREATE_ONE_TIME_CARD.handleCreateOneTimeCard(users, cmd.getEmail(),
                            cmd.getAccount(), cmd.getTimestamp());
                    break;
                case "deleteCard":
                    DELETE_CARD.handleDeleteCard(users, cmd.getCardNumber(), cmd.getTimestamp());
                    break;
                case "payOnline":
                    PAY_ONLINE.handlePayOnline(objectMapper, output, users, cmd.getEmail(),
                            cmd.getCardNumber(), cmd.getAmount(), cmd.getCurrency(),
                            cmd.getCommerciant(), cmd.getTimestamp(),
                            Arrays.asList(inputData.getCommerciants()));
                    break;
                case "sendMoney":
                    SEND_MONEY.handleSendMoney(objectMapper, output, users,
                            cmd.getAccount(),
                            cmd.getReceiver(),
                            cmd.getAmount(),
                            cmd.getEmail(),
                            cmd.getDescription(),
                            cmd.getTimestamp());
                    break;
                case "printTransactions":
                    PRINT_TRANSACTIONS.handlePrintTransactions(objectMapper, output, users,
                            cmd.getEmail(), cmd.getTimestamp());
                    break;
                case "splitPayment":
                    SPLIT_PAYMENT.handleSplitPayment(users, cmd.getAccounts(), cmd.getAmount(),
                            cmd.getCurrency(), cmd.getTimestamp(),
                            List.of(inputData.getExchangeRates()));
                    break;
                case "report":
                    REPORT.handleReport(objectMapper, output, users, cmd.getAccount(),
                            cmd.getStartTimestamp(), cmd.getEndTimestamp(),
                            cmd.getTimestamp());
                    break;
                case "setMinimumBalance":
                    MINIMUM_BALANCE.handleSetMinimumBalance(users, cmd.getAccount(),
                            cmd.getAmount());
                    break;
                case "checkCardStatus":
                    CARD_STATUS.handleCheckCardStatus(objectMapper, output, users,
                            cmd.getCardNumber(), cmd.getTimestamp());
                    break;
                    case "withdrawSavings":
                        WITHDRAW_SAVINGS.handleWithdrawSavings(users,
                                cmd.getAccount(),
                                cmd.getAmount(),
                                cmd.getCurrency(), cmd.getTimestamp());
                        break;
                case "upgradePlan" :
                    UPGRADE_PLAN.handleUpgradePlan(objectMapper,
                            output, users,
                            cmd.getAccount(),
                            cmd.getNewPlanType(), cmd.getTimestamp());
                    break;
                case "cashWithdrawal" :
                    CASH_WITHDRAWAL.handleCashWithdrawal(objectMapper,
                            output, users,
                            cmd.getEmail(),
                            cmd.getCardNumber(),
                            cmd.getAmount(), cmd.getTimestamp());
                    break;
                case "addInterest" :
                    ADD_INTEREST.handleAddInterest(users, cmd.getAccount(), cmd.getTimestamp());
                    break;
                default:
                    break;
            }
        }

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         * ObjectMapper mapper = new ObjectMapper();
         *
         * ObjectNode objectNode = mapper.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = mapper.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        return Integer.parseInt(
                file.getName()
                        .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR)
        );
    }
}
