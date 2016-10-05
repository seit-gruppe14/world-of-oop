package zuulFramework.worldofzuul;

import java.util.List;

/**
 * Created by Rasmus Hansen .
 */
public class Exit extends Room implements ICanPay {

    public Exit(String description) {
        super(description);
    }

    @Override
    public boolean buy(Player player, Command command, Game game) {
        
       List<Item> itemsToBuy = player.getItems();
       int sum = 0;
       for (int i = 0; i < itemsToBuy.size(); i++) {
            Item item = itemsToBuy.get(i);
            
                    
        }

        return false;
    }


}
