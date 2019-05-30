import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tradingBot {
    public static Long epoch;
    public static boolean Botalive = true;
    public static ChromeDriver driver;
    public static boolean er = false;
    public static Integer NumberOfAccount = 1;
    public static Integer CurrentAcc = 1;


    public static void statistic(String api, String url_statistic) {

        boolean limit = false;
        WebElement Limit, MoneyOnAcc;
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        JSONParser jsonParser = new JSONParser();

        String OlympEnd = ((ChromeDriver) driver).findElementByClassName("strike-button-price").getText();
        params2.add(new BasicNameValuePair("api", api));
        params2.add(new BasicNameValuePair("token", ""));
        params2.add(new BasicNameValuePair("EndPrice", OlympEnd));

        try {
            jsonParser.httpsConnection(url_statistic, params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkMoney(String api, String vpsNumber, String url) {

        boolean limit = false;
        WebElement Limit, MoneyOnAcc;
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        JSONParser jsonParser = new JSONParser();

        MoneyOnAcc = ((ChromeDriver) driver).findElementByClassName("header-row__balance-sum");
        params2.add(new BasicNameValuePair("api", api));
        params2.add(new BasicNameValuePair("token", ""));
        params2.add(new BasicNameValuePair("money", MoneyOnAcc.getText()));
        params2.add(new BasicNameValuePair("VpsNumber", vpsNumber));
        params2.add(new BasicNameValuePair("Account", String.valueOf(CurrentAcc)));
        try {
            jsonParser.httpsConnection(url, params2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkLimit(String api) {

        boolean limit = false;
        WebElement Limit, MoneyOnAcc;
        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        JSONParser jsonParser = new JSONParser();
        try {
            Limit = ((ChromeDriver) driver).findElementByClassName("notice-top");
            if (Limit != null) {
                MoneyOnAcc = ((ChromeDriver) driver).findElementByClassName("header-row__balance-sum");
                epoch = System.currentTimeMillis() / 1000;
                params1.add(new BasicNameValuePair("api", api));
                params1.add(new BasicNameValuePair("token", "vnerkvnnvaqloui48"));
                params1.add(new BasicNameValuePair("money", MoneyOnAcc.getText()));

                limit = true;
                try {
                    jsonParser.httpsConnection("https://medportrus.ru/tradingbot/stoptrading.php", params1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params1.clear();
            }

        } catch (NoSuchElementException ert) {
            limit = false;

        }
        return limit;
    }


    public static String getSHA(String input) {
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }

    public static String removeCharAt(String s1, int pos) {
        return s1.substring(0, pos) + s1.substring(pos + 1);
    }

    public static void traiding(String[] args) {
        String[] config = new String[11];  // 2 - apikey, 4 - login, 6 - password, 8 - path to chrome client, 10 - path to driver
        int i = 0;
        int alive = 0;
        String path = "";
        JSONParser jsonParser1 = new JSONParser();
        WebElement Price, Percent, PlusButton, LongButton, ShortButton, Limit, MoneyOnAcc;
        String log, pass, TraidingPair;
        path = args[0];
        JSONParser jsonParser = new JSONParser();
        String url_send = "https://medportrus.ru/tradingbot/customtrade.php";
        String url_limit = "https://medportrus.ru/tradingbot/stoptrading.php";
        String url_statistic = "";
        JSONObject json = null;
        int bet;
        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                config[i] = strLine;
                i = i + 1;
            }
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
        log = config[3];
        pass = config[5];
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

        System.setProperty("webdriver.chrome.driver", config[9]); //win server
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + config[7]);
        options.addArguments("--start-maximized");
        //  options.addArguments("--no-sandbox"); // для линукса!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://olymptrade.com/");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            PlusButton = ((ChromeDriver) driver).findElementById("pair-managing-add-btn");
        } catch (NoSuchElementException we) {

            WebElement lgnBtn;
            try {
                lgnBtn = ((ChromeDriver) driver).findElementByClassName("satir-button");
                lgnBtn.click();
            } catch (NoSuchElementException ex) {
                System.out.println("zrazy v logine");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebElement login;
            WebElement password;
            try {
                System.out.println("ishet login");
                login = ((ChromeDriver) driver).findElementByName("email");
                System.out.println("ishet parol");
                password = ((ChromeDriver) driver).findElementByName("password");
                login.sendKeys(log);
                password.sendKeys(pass);
                password.sendKeys(Keys.ENTER);
                // System.out.println("sleep");
            } catch (NoSuchElementException ex12) {
                System.out.println("zashel bez logina");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////залогинились
        System.out.println("Bot starts");
        int ir = 0;

        Date date;
        LongButton = ((ChromeDriver) driver).findElementByClassName("deal-buttons__button_up");
        ShortButton = ((ChromeDriver) driver).findElementByClassName("deal-buttons__button_down");

        //при включении
        epoch = System.currentTimeMillis() / 1000;
        params.add(new BasicNameValuePair("api", config[1]));
        params.add(new BasicNameValuePair("time", epoch.toString()));
        params.add(new BasicNameValuePair("hash", ""));


        params.add(new BasicNameValuePair("percent", "40"));  //40
        params.add(new BasicNameValuePair("asksbids", "1.5")); //1.5
        params.add(new BasicNameValuePair("difference", "3"));//3
        params.add(new BasicNameValuePair("DOM", "3"));//3


        try {
            json = jsonParser.httpsConnection(url_send, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.clear();

        try {
            Integer error = json.getInt("error");
            if ((error == 0) || (error == 6)) {

                while (ir == 0) {
                    TraidingPair = ((ChromeDriver) driver).findElementByClassName("pair-tab_selected").findElement(By.className("title")).getText();

                    if (TraidingPair.equals("Bitcoin")) {

                        Price = ((ChromeDriver) driver).findElementByClassName("strike-button-price");
                        Percent = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text");
                        epoch = System.currentTimeMillis() / 1000;

                        params.add(new BasicNameValuePair("api", config[1]));
                        params.add(new BasicNameValuePair("price", Price.getText()));
                        params.add(new BasicNameValuePair("percent", Percent.getText()));
                        params.add(new BasicNameValuePair("hash", getSHA(epoch.toString() + config[1])));
                        try {
                            json = jsonParser.httpsConnection(url_send, params);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            Integer success = json.getInt("error");
                            // System.out.println(json.toString() + "  ");  //убрать на релизе
                            switch (success) {
                                case 0:
                                    bet = json.getInt("bet");
                                    switch (bet) {
                                        case 1:
                                            String Percent12 = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text").getText();
                                            Percent12 = removeCharAt(Percent12, 2);
                                            if (Integer.valueOf(Percent12) >= Integer.valueOf(removeCharAt(Percent.getText(), 2))) {
                                                LongButton.click();
                                                date = new Date();
                                                System.out.println("Long bet " + "Bitcoin price: " + Price.getText() + "  " + date.toString());

                                                er = checkLimit(config[1]);


                                            }
                                            ////////////////////////////// new for statistic
                                            try {
                                                Thread.sleep(60000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            params2.add(new BasicNameValuePair("EndPrice", ((ChromeDriver) driver).findElementByClassName("strike-button-price").getText()));
                                            try {
                                                jsonParser.httpsConnection(url_statistic, params2);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            params2.clear();
                                            try {
                                                Thread.sleep(60000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            ////////////////////////////// end
                                            break;
                                        case 2:
                                            String Percent13 = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text").getText();
                                            Percent13 = removeCharAt(Percent13, 2);
                                            if (Integer.valueOf(Percent13) >= Integer.valueOf(removeCharAt(Percent.getText(), 2))) {
                                                ShortButton.click();
                                                date = new Date();
                                                System.out.println("Short bet " + "Bitcoin price: " + Price.getText() + "  " + date.toString());

                                                er = checkLimit(config[1]);
                                            }
                                            //////////////////////////////////////// new for statistic
                                            try {
                                                Thread.sleep(60000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            params2.add(new BasicNameValuePair("EndPrice", ((ChromeDriver) driver).findElementByClassName("strike-button-price").getText()));
                                            try {
                                                jsonParser.httpsConnection(url_statistic, params2);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            params2.clear();
                                            try {
                                                Thread.sleep(60000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            ///////////////////////////////////////// end
                                            break;
                                        case 0:
                                            // что делать если нет сделки
                                            break;
                                    }
                                    break;
                                case 1:
                                    System.out.println("Api key error, please check config.txt");
                                    break;
                                case 2:
                                    System.out.println("program version error");
                                    break;
                                case 3:
                                    //
                                    break;
                                case 4:
                                    System.out.println("your api key is being used from another ip address");
                                    break;
                                case 6:
                                    //System.out.println("Time error");
                                    break;
                            }


                        } catch (NullPointerException e) {
                            System.out.println("server is not available");
                        }

                        if (er == true) {
                            break;
                        }
                        params.clear();
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // если торговая пара не биткоин
                    }
                }
                System.out.println("Bot was stopped");

            } else {
                System.out.println("не тот еррор  " + error.toString());
            }

        } catch (NullPointerException e132) {
            System.out.println("Connection to server error");
        }
        System.out.println("end");

    }

    public static void traidingNewOlymp(String[] args, String login1, String passw, String Path, Integer number) {
        String[] config = new String[35];  // 2 - apikey, 4 - login, 6 - password, 8 - path to chrome client, 10 - path to driver
        int i = 0;
        int alive = 0;
        String path = "";
        JSONParser jsonParser1 = new JSONParser();
        WebElement Price, Percent, PlusButton, LongButton = null, ShortButton = null, Limit, MoneyOnAcc;
        String log, pass, TraidingPair;
        path = args[0];
        JSONParser jsonParser = new JSONParser();
        String url_send = "https://hfahuq.xyz/tradingbot/ii1.php";
        String url_send_money_amount = "https://hfahuq.xyz/tradingbot1/moneycheck.php";
        String url_limit = "https://hfahuq.xyz/tradingbot/stoptrading.php";
        String url_statistic = "https://hfahuq.xyz/tradingbot/ii2.php";
        JSONObject json = null;
        int bet;
        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                config[i] = strLine;
                i = i + 1;
            }
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
        log = login1;
        pass = passw;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        List<NameValuePair> params123 = new ArrayList<NameValuePair>();
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

        System.setProperty("webdriver.chrome.driver", config[3]); //win server
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + Path);
        options.addArguments("--start-maximized");

        switch (number) {
            case 1:
                // options.addArguments("--proxy-server=http://" + config[25]);
                options.addArguments("profile-directory=Default");
                break;
            case 2:
                options.addArguments("--proxy-server=http://" + config[27]);
                options.addArguments("profile-directory=Profile 1");
                break;
            case 3:
                options.addArguments("--proxy-server=http://" + config[29]);
                options.addArguments("profile-directory=Profile 2");
                break;
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://olymptrade.com/");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            PlusButton = ((ChromeDriver) driver).findElementById("pair-managing-add-btn");
        } catch (NoSuchElementException we) {

            WebElement lgnBtn;
            try {
                lgnBtn = ((ChromeDriver) driver).findElementByClassName("satir-button");
                lgnBtn.click();
            } catch (NoSuchElementException ex) {
                System.out.println("zrazy v logine");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebElement login;
            WebElement password;
            try {
                System.out.println("ishet login");
                login = ((ChromeDriver) driver).findElementByName("email");
                System.out.println("ishet parol");
                password = ((ChromeDriver) driver).findElementByName("password");
                login.sendKeys(log);
                password.sendKeys(pass);
                password.sendKeys(Keys.ENTER);
                // System.out.println("sleep");
            } catch (NoSuchElementException ex12) {
                System.out.println("zashel bez logina");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////залогинились
        System.out.println("Bot starts");
        int ir = 0;

        Date date;

        do {
            try {
                LongButton = ((ChromeDriver) driver).findElementByClassName("deal-buttons__button_up");
                ShortButton = ((ChromeDriver) driver).findElementByClassName("deal-buttons__button_down");
            } catch (NoSuchElementException e34) {
                driver.navigate().refresh();
            }

        }
        while (LongButton == null);


        //при включении
        epoch = System.currentTimeMillis() / 1000;
        params.add(new BasicNameValuePair("api", config[1]));
        params.add(new BasicNameValuePair("time", epoch.toString()));
        params.add(new BasicNameValuePair("hash", ""));

        params.add(new BasicNameValuePair("VpsNumber", config[23]));

        params.add(new BasicNameValuePair("percent", "40"));  //40
        params.add(new BasicNameValuePair("asksbids", "1.5")); //1.5
        params.add(new BasicNameValuePair("difference", "3"));//3
        params.add(new BasicNameValuePair("DOM", "3"));//3

        try {
            json = jsonParser.httpsConnection(url_send, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.clear();
        try {
            Integer error = json.getInt("error");
            if ((error == 0) || (error == 6)) {
                while (ir == 0) {
                    TraidingPair = ((ChromeDriver) driver).findElementByClassName("pair-assets-select-title").getText();
                    if (TraidingPair.equals("Bitcoin")) {
                        Price = ((ChromeDriver) driver).findElementByClassName("strike-button-price");
                        Percent = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text");
                        epoch = System.currentTimeMillis() / 1000;
                        params.add(new BasicNameValuePair("api", config[1]));
                        params.add(new BasicNameValuePair("price", Price.getText()));
                        params.add(new BasicNameValuePair("percent", Percent.getText()));
                        params.add(new BasicNameValuePair("VpsNumber", config[23]));
                        params.add(new BasicNameValuePair("hash", getSHA(epoch.toString() + config[1])));
                        try {
                            json = jsonParser.httpsConnection(url_send, params);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Integer success = json.getInt("error");
                            // System.out.println(json.toString() + "  ");  //убрать на релизе
                            switch (success) {
                                case 0:
                                    bet = json.getInt("bet");
                                    switch (bet) {
                                        case 1:
                                            String Percent12 = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text").getText();
                                            Percent12 = removeCharAt(Percent12, 2);
                                            if (Integer.valueOf(Percent12) >= Integer.valueOf(removeCharAt(Percent.getText(), 2))) {
                                                LongButton.click();
                                                date = new Date();
                                                System.out.println("Long bet " + "Bitcoin price: " + Price.getText() + "  " + date.toString());
                                                er = checkLimit(config[1]);
                                                NumberOfAccount = json.getInt("Account");
                                                System.out.println(NumberOfAccount);
                                            }

                                            if (NumberOfAccount != CurrentAcc) {
                                                try {
                                                    Thread.sleep(60000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                statistic(config[1], url_statistic);
                                                try {
                                                    Thread.sleep(28000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    Thread.sleep(120000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            checkMoney(config[1], config[23], url_send_money_amount);
                                            break;
                                        case 2:
                                            String Percent13 = ((ChromeDriver) driver).findElementByClassName("deal-buttons__text").getText();
                                            Percent13 = removeCharAt(Percent13, 2);
                                            if (Integer.valueOf(Percent13) >= Integer.valueOf(removeCharAt(Percent.getText(), 2))) {
                                                ShortButton.click();
                                                date = new Date();
                                                System.out.println("Short bet " + "Bitcoin price: " + Price.getText() + "  " + date.toString());
                                                er = checkLimit(config[1]);
                                                NumberOfAccount = json.getInt("Account");
                                                System.out.println(NumberOfAccount);
                                            }
                                            if (NumberOfAccount != CurrentAcc) {
                                                try {
                                                    Thread.sleep(60000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                statistic(config[1], url_statistic);
                                                try {
                                                    Thread.sleep(28000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    Thread.sleep(120000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            checkMoney(config[1], config[23], url_send_money_amount);
                                            break;
                                        case 0:
                                            // что делать если нет сделки
                                            break;
                                    }
                                    break;
                                case 1:
                                    System.out.println("Api key error, please check config.txt");
                                    break;
                                case 2:
                                    System.out.println("program version error");
                                    break;
                                case 3:
                                    //
                                    break;
                                case 4:
                                    System.out.println("your api key is being used from another ip address");
                                    break;
                                case 6:
                                    //System.out.println("Time error");
                                    break;
                            }


                        } catch (NullPointerException e) {
                            System.out.println("server is not available");
                        }

                        if (er == true) {
                            break;
                        }
                        params.clear();
                        if (NumberOfAccount != CurrentAcc) {
                            System.out.println("Num " + NumberOfAccount + "    Cur " + CurrentAcc);
                            break;
                        }
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // если торговая пара не биткоин
                    }
                }
                System.out.println("Bot was stopped");

            } else {
                System.out.println("не тот еррор  " + error.toString());
            }

        } catch (NullPointerException e132) {
            System.out.println("Connection to server error");
        }
        System.out.println("end");
    }

    public static void main(String[] args) {
        int i = 0;
        String[] config = new String[40];
        try {
            FileInputStream fstream = new FileInputStream(args[0]);
            // FileInputStream fstream = new FileInputStream("config.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                config[i] = strLine;
                i = i + 1;
            }
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
        String[] login = new String[3];
        String[] pass = new String[3];
        String[] GmailPath = new String[3];
        login[0] = config[5];
        pass[0] = config[7];
        GmailPath[0] = config[9];
        login[1] = config[11];
        pass[1] = config[13];
        GmailPath[1] = config[15];
        login[2] = config[17];
        pass[2] = config[19];
        GmailPath[2] = config[21];
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                traidingNewOlymp(args, login[0], pass[0], GmailPath[0], 1);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (i == 0) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Checker check = new Checker();
                    Botalive = check.checkTime(epoch);
                    if ((Botalive == false) && (NumberOfAccount != CurrentAcc)) {
                        driver.close();
                        thread1.interrupt();
                        if (er == false) {

                            Thread thread1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (NumberOfAccount) {
                                        case 1:
                                            CurrentAcc = NumberOfAccount;
                                            traidingNewOlymp(args, login[0], pass[0], GmailPath[0], 1);
                                            break;
                                        case 2:
                                            CurrentAcc = NumberOfAccount;
                                            traidingNewOlymp(args, login[1], pass[1], GmailPath[1], 2);
                                            break;
                                        case 3:
                                            CurrentAcc = NumberOfAccount;
                                            traidingNewOlymp(args, login[2], pass[2], GmailPath[2], 3);
                                            break;
                                    }
                                    //traiding(args);
                                    //traidingNewOlymp(args, login[0], pass[0], GmailPath[0], 0);
                                }
                            });
                            thread1.start();
                        }
                    } else {
                        //System.out.println("all ok");
                    }
                }
            }
        });
        thread.start();
        thread1.start();
    }
}