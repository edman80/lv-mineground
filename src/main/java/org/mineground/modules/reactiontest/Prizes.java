package org.mineground.modules.reactiontest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.YamlConfiguration;

public class Prizes {
    private YamlConfiguration configuration;
    private List<Prize> prizeList;
    private Random randomGenerator;

    public Prizes(YamlConfiguration configuration) {
        this.configuration = configuration;
        prizeList = new ArrayList<Prize>();
        
        generatePrizeList();
    }

    private void generatePrizeList() {
        String[] itemStringSplit;
        for (String prizeString : configuration.getStringList("prizes")) {
            itemStringSplit = prizeString.split(",");
            prizeList.add(new Prize(Integer.parseInt(itemStringSplit[1]), Integer.parseInt(itemStringSplit[2]), itemStringSplit[0]));
        }
    }
    
    public Prize getRandomPrize() {
        return prizeList.get(randomGenerator.nextInt(prizeList.size()));
    }

    class Prize {
        private int itemId;
        private int itemAmount;
        private String itemName;
        
        public Prize(int itemId, int itemAmount, String itemName) {
            this.itemId = itemId;
            this.itemAmount = itemAmount;
            this.itemName = itemName;
        }

        public int getItemId() {
            return itemId;
        }

        public int getItemAmount() {
            return itemAmount;
        }

        public String getItemName() {
            return itemName;
        }
    }
}
