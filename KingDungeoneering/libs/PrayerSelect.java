package libs;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class PrayerSelect {

	
	
	private int hexActivation = 0;
	private WidgetChild prayerWidget = null;

	public PrayerSelect(int hexIsOn, int widgetID) {
		prayerWidget = Widgets.get(271).getChild(7).getChild(widgetID);
		hexActivation = hexIsOn;
	}

	public void setPrayer(boolean b) {
		if (Tabs.getCurrent() != Tabs.PRAYER) {
			Tabs.PRAYER.open();
			Mouse.hop(prayerWidget.getAbsoluteX(), prayerWidget.getAbsoluteY());
			if (b == true) {
				if (Menu.contains("Activate")) {
					if (prayerWidget.interact("Activate")) {
					}
				} else {
					return;
				}
			} else {
				if (Menu.contains("Deactivate")) {
					if (prayerWidget.interact("Deactivate")) {
					}
				}
			}
		}
	}

	public boolean isActivated() {
		return Settings.get(1395) == hexActivation;
	}
}


