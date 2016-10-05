package zuulFramework.worldofzuul;

/**
 * Created by Rasmus Hansen .
 */
public class Exit extends Room implements ICanPay {

    public Exit(String description) {
        super(description);
    }

    @Override
    public boolean buy(Player player, Command command) {

        return false;
    }


}
