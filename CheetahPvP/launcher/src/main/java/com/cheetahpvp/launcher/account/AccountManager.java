package com.cheetahpvp.launcher.account;

import com.cheetahpvp.launcher.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saved accounts (offline and premium)
 */
public class AccountManager {

    private static AccountManager instance;
    private List<Account> accounts;
    private Account selectedAccount;
    private File accountsFile;
    private Gson gson;

    private AccountManager() {
        accounts = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        accountsFile = new File(Main.DATA_DIR, "accounts.json");
        load();
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public void addAccount(Account account) {
        // Remove existing account with same username
        accounts.removeIf(a -> a.getUsername().equalsIgnoreCase(account.getUsername()));
        accounts.add(account);
        save();
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        save();
    }

    private void load() {
        if (accountsFile.exists()) {
            try (FileReader reader = new FileReader(accountsFile)) {
                Type listType = new TypeToken<ArrayList<Account>>() {
                }.getType();
                List<Account> loaded = gson.fromJson(reader, listType);
                if (loaded != null) {
                    accounts = loaded;
                }
            } catch (Exception e) {
                System.err.println("Failed to load accounts: " + e.getMessage());
            }
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(accountsFile)) {
            gson.toJson(accounts, writer);
        } catch (Exception e) {
            System.err.println("Failed to save accounts: " + e.getMessage());
        }
    }
}
