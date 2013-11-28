package doors;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.event.MessageEvent;


public class SkillDoor extends DGDoor {

	
	private final String INTR_AGIL = "Disarm";
	private final String INTR_WC   = "Chop-down";
	private final String INTR_MINE = "Destroy";
	private final String INTR_MAG  = "Dispel";
	private final String INTR_SMITH= "Repair-key";
	private final String INTR_FM   = "Burn";
	private final String INTR_SUM  = "Dismiss";
	private final String INTR_HERB = "Add-compound";
	private final String INTR_FARM = "Prune-vines";
	private final String INTR_CONS = "Repair";
	private final String INTR_PRAY = "Exorcise";
	private final String INTR_RC   = "Imbue-energy";
	private final String INTR_CRAFT= "Fix-pulley";
	private final String INTR_THEIF= "Pick-lock";
	private final String INTR_STR  = "Force";
	
	private final String MSG_AGIL = "You grab the chain";
	private final String MSG_WC   = "You chop down";
	private final String MSG_MINE = "You mine the";
	private final String MSG_MAG  = "You dispel the";
	private final String MSG_SMITH= "You reforge";
	private final String MSG_FM   = "You burn";
	private final String MSG_SUM  = "You dismiss";
	private final String MSG_HERB = "You mix";
	private final String MSG_FARM = "You carefully";
	private final String MSG_CONS = "You repair the door";
	private final String MSG_PRAY = "You exorcise";
	private final String MSG_RC   = "You successfully imbue";
	private final String MSG_CRAFT= "You repair the pulley";
	private final String MSG_THEIF= "You successfully pick";
	private final String MSG_STR  = "You pull";
	
	private final String DR_AGIL = "Locked door";
	private final String DR_WC   = "Wooden barricade";
	private final String DR_MINE = "Pile of rocks";
	private final String DR_MAG  = "Magic barrier";
	private final String DR_SMITH= "Broken key door";
	private final String DR_FM   = "Flammable debris";
	private final String DR_SUM  = "Ramokee exile";
	private final String DR_HERB = "Liquid lock door";
	private final String DR_FARM = "Vine-covered door";
	private final String DR_CONS = "Collapsing doorframe";
	private final String DR_PRAY = "Dark spirit";
	private final String DR_RC   = "Runed door";
	private final String DR_CRAFT= "Broken pulley door";
	private final String DR_THEIF= "Padlocked door";
	private final String DR_STR  = "Barred door";
	
	private int skill;
	private int req;
	
	private String cm;
	private String io;
	
	public SkillDoor(int doorID, String doorName, Tile loc, String open) {
		super(doorID, doorName, loc);
		
		if      (doorName.equals(DR_AGIL)) { skill = Skills.AGILITY ;}
		else if (doorName.equals(DR_WC))   { skill = Skills.WOODCUTTING;}
		else if (doorName.equals(DR_MINE)) { skill = Skills.MINING;}
		else if (doorName.equals(DR_MAG))  { skill = Skills.MAGIC;}
		else if (doorName.equals(DR_SMITH)){ skill = Skills.SMITHING;}
		else if (doorName.equals(DR_FM))   { skill = Skills.FIREMAKING;}
		else if (doorName.equals(DR_SUM))  { skill = Skills.SUMMONING;}
		else if (doorName.equals(DR_HERB)) { skill = Skills.HERBLORE;}
		else if (doorName.equals(DR_FARM)) { skill = Skills.FARMING;}
		else if (doorName.equals(DR_CONS)) { skill = Skills.CONSTRUCTION;}
		else if (doorName.equals(DR_PRAY)) { skill = Skills.PRAYER;}
		else if (doorName.equals(DR_RC))   { skill = Skills.RUNECRAFTING;}
		else if (doorName.equals(DR_CRAFT)){ skill = Skills.CRAFTING;}
		else if (doorName.equals(DR_THEIF)){ skill = Skills.THIEVING;}
		else if (doorName.equals(DR_STR))  { skill = Skills.STRENGTH;}
		
		
		setSkill(skill);
	}
	
	public void setRequirement(int i) {
		this.req = i;
	}

	private void setSkill(int skill) {
		switch (skill) {
		case Skills.AGILITY:
			
			io = INTR_AGIL;
			cm = MSG_AGIL;
			break;
			
		case Skills.WOODCUTTING:
			
			io = INTR_WC;
			cm = MSG_WC;
			break;
			
		case Skills.MINING:
			
			io = INTR_MINE;
			cm = MSG_MINE;
			break;
			
		case Skills.MAGIC:

			io = INTR_MAG;
			cm = MSG_MAG;
			break;
			
		case Skills.SMITHING:
			
			io = INTR_SMITH;
			cm = MSG_SMITH;
			break;
			
		case Skills.FIREMAKING:
			
			io = INTR_FM;
			cm = MSG_FM;
			break;
			
		case Skills.SUMMONING:
			
			io = INTR_SUM;
			cm = MSG_SUM;
			break;
			
		case Skills.HERBLORE:
			
			io = INTR_HERB;
			cm = MSG_HERB;
			break;
			
		case Skills.FARMING:
			
			io = INTR_FARM;
			cm = MSG_FARM;
			break;
			
		case Skills.CONSTRUCTION:
			
			io = INTR_CONS;
			cm = MSG_CONS;
			break;
			
		case Skills.PRAYER:
			
			io = INTR_PRAY;
			cm = MSG_PRAY;
			break;
			
		case Skills.RUNECRAFTING:
			
			io = INTR_RC;
			cm = MSG_RC;
			break;
			
		case Skills.CRAFTING:
			
			io = INTR_CRAFT;
			cm = MSG_CRAFT;
			break;
			
		case Skills.THIEVING:
			
			io = INTR_THEIF;
			cm = MSG_THEIF;
			break;
			
		case Skills.STRENGTH:
			
			io = INTR_STR;
			cm = MSG_STR;
			break;
			
		}
	}
	
	public boolean hasRequirements() {
		return Skills.getLevel(skill) >= req;
	}
	
	public int getSkill() {
		return skill;
	}
	
	public int getRequirement() {
		return req;
	}
	
	
	public void openDoor() {
		SceneObject d = SceneEntities.getNearest(doorFilter);
		if (d != null) {
			if (!d.isOnScreen()) {
				Walking.walk(d);
				Camera.turnTo(d);
			}
			d.interact(io);
		}
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		
		String msg = arg0.getMessage();
		
		if (msg.equals( MSG_AGIL ) 
		|| msg.equals( MSG_WC  )
		|| msg.equals( MSG_MINE )
		|| msg.equals( MSG_MAG )
		|| msg.equals( MSG_SMITH)
		|| msg.equals( MSG_FM   )
		|| msg.equals( MSG_SUM )
		|| msg.equals( MSG_HERB )
		|| msg.equals( MSG_FARM)
		|| msg.equals( MSG_CONS )
		|| msg.equals( MSG_PRAY)
		|| msg.equals( MSG_RC  )
		|| msg.equals( MSG_CRAFT)
		|| msg.equals( MSG_THEIF)
		|| msg.equals( MSG_STR )) {
			this.isOpen = true;
		}
	}
}
