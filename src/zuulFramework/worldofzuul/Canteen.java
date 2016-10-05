package zuulFramework.worldofzuul;

/**
 * Created by Rasmus Hansen .
 */
public class Canteen extends Room implements ICanPay {

    /**
     * @param description
     */
    public Canteen(String description) {
        super(description);
    }

    @Override
    public boolean buy(Player player, Command command) {
        return false;
    }
}
