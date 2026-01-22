import eathubUI.EatHubUI;
import eathubDL.DAOconfig;

public class Main {
    public static void main(String[] args) throws Exception {
        DAOconfig.CreateBD();
        EatHubUI ui = new EatHubUI();
        ui.run();
    }
}
