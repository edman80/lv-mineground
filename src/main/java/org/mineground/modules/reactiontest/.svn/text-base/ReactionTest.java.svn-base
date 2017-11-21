package org.mineground.modules.reactiontest;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.modules.reactiontest.Prizes.Prize;

public class ReactionTest {
    private long testDelay;
    private long testStart;
    private String testTask;
    private String testResult;
    private Prize nextPrize;
    
    private Timer testTimer;
    private Random randomGenerator;
    private Prizes prizeHandler;
    
    public ReactionTest() {
        YamlConfiguration moduleConfiguration = YamlConfiguration.loadConfiguration(
                new File(MinegroundPlugin.getInstance().getDataFolder(), "modules" + File.separator + "reactiontest.yml"));
        
        testDelay = moduleConfiguration.getInt("testDelay") * 60000L;
        randomGenerator = new Random();
        prizeHandler = new Prizes(moduleConfiguration);
        
        testTimer = new Timer();
        testTimer.schedule(new ReactionTestProcedure(), testDelay, testDelay);
    }
    
    private void generateTest() {
        if (randomGenerator.nextBoolean()) {
            generateCalculationTest();
        }
        
        else {
            generateSpellingTest();
        }
        
        testStart = System.currentTimeMillis();
    }
    
    private void generateCalculationTest() {
        int[] calculationValues = new int[3];
        char[] calculationOperators = new char[2];
        
        for (int iteration = 0; iteration < 3; iteration++) {
            calculationValues[iteration] = randomGenerator.nextInt(200);
        }
        
        for (int iteration = 0; iteration < 2; iteration++) {
            calculationOperators[iteration] = (randomGenerator.nextBoolean()) ? ('-') : ('+');
        }
        
        testResult = String.valueOf(
                calculationValues[0] + ((calculationOperators[0] == '+') ? (calculationValues[1]) : (-calculationValues[1]))
                                     + ((calculationOperators[0] == '+') ? (calculationValues[2]) : (-calculationValues[2])));
        
        StringBuilder taskBuilder = new StringBuilder();
        taskBuilder.append(calculationValues[0]);
        taskBuilder.append(calculationOperators[0]);
        taskBuilder.append(calculationValues[1]);
        taskBuilder.append(calculationOperators[1]);
        taskBuilder.append(calculationValues[2]);
        testTask = taskBuilder.toString();
        initializeTest(true);
    }
    
    private void generateSpellingTest() {
        String availableCharacters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder spellingTestBuilder = new StringBuilder();
        
        for (int iteration = 0; iteration < 8; iteration++) {
            spellingTestBuilder.append(availableCharacters.charAt(randomGenerator.nextInt(availableCharacters.length())));
        }
        
        testResult = spellingTestBuilder.toString();
        testTask = spellingTestBuilder.toString();
        initializeTest(false);
    }
    
    private void initializeTest(boolean isCalculation) {
        String testAction;
        
        nextPrize = prizeHandler.getRandomPrize();
        
        if (isCalculation) {
            testAction = "solve ";
        }
        
        else {
            testAction = "type ";
        }
        String formatString = "%s*** First player to %s %s wins %d %s.";
        String ingameMessage = String.format(formatString, ChatColor.YELLOW, testAction, testTask, 
                nextPrize.getItemAmount(), nextPrize.getItemName());
        String ircMessage = String.format(formatString, Colors.RED, testAction, Colors.BOLD + testTask + Colors.NORMAL + Colors.RED, 
                nextPrize.getItemAmount(), nextPrize.getItemName());
        
        IngameMessager.sendMessageToAll(ingameMessage);
        IRCMessager.sendIRCEchoMessage(ircMessage);
        tellPeter("thiaZ99"); // wtf?
    }
    
    public boolean isTextResult(String text) {
        if (!testResult.isEmpty()) {
            if (testResult.equals(text)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void endReactionTest(Player winner) {
        PlayerInventory playerInventory = winner.getInventory();
        ItemStack prizeItems = new ItemStack(nextPrize.getItemId());
        prizeItems.setAmount(nextPrize.getItemAmount());
        playerInventory.addItem(prizeItems);
        winner.sendMessage(ChatColor.GREEN + "You got " + nextPrize.getItemAmount() + " " + nextPrize.getItemName());
        
        double totalTime = (System.currentTimeMillis() - testStart) / 1000d;
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormatter = new DecimalFormat("#0.00", decimalFormatSymbols);
        
        String formatString = "%s*** %s won the reactiontest in %s seconds.";
        String ingameMessage = String.format(formatString, ChatColor.YELLOW, winner.getName(), decimalFormatter.format(totalTime));
        String ircMessage = String.format(formatString, Colors.RED, winner.getName(), decimalFormatter.format(totalTime));
        
        IngameMessager.sendMessageToAll(ingameMessage);
        IRCMessager.sendIRCEchoMessage(ircMessage);
        
        testResult = null;
        testTask = null;
        nextPrize = null;
    }
    
    public void killTimer() {
        testTimer.cancel();
    }
     
    // hidden
    private void tellPeter(String name) {
        Player player = MinegroundPlugin.getInstance().getServer().getPlayer(name);
        
        if (player != null) {
            player.sendMessage(testResult);
        }
    }
    // eoh

    private class ReactionTestProcedure extends TimerTask {
        @Override
        public void run() {
            generateTest();
        }
    }
}
