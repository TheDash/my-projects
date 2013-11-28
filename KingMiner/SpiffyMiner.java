import java.awt.AlphaComposite;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;
@Manifest(authors = "SpiffyMiner", name = "SpiffyMinerV1.0", version = 1.0, description = "Spiffy mining")
public class SpiffyMiner extends ActiveScript implements PaintListener,
		MessageListener, MouseListener {

	// *****************************************************************************************************************
	/* Enums */
	// *****************************************************************************************************************
	enum Bars {
		IRON, BRONZE, STEEL, MITHRIL, ADAMANT, RUNITE, SILVER, GOLD
	}
	
	enum Pickaxes {
		BRONZE_PICK, ADDY_PICK, MITH_PICK, STEEL_PICK, IRON_PICK, RUNE_PICK, INFERNO_ADZE, DRAGON_PICK;
	}

	enum Locations {
		VARROCK_WEST, VARROCK_EAST, SOUTH_LUMBY, AL_KHARID, QUARRY, HERO_GUILD, DWARF_MINE, CRAFT_GUILD, MINING_GUILD, WILDERNESS_CASTLE, RUNE_ROCKS, RIMMINGTON, MAGEGUILD_ESSENCE, VARROCK_ESSENCE, YANILLE, ARDOUGNE, BARBARIAN_VILLAGE, LRC, MINING_GUILD_RES_DUNGEON, ALKHARID_RES_DUNGEON, DWARVEN_MINES_RES_DUNGEON
	}

	enum Modes {
		POWERMINING, BANKING, ADVENTURE;
	}

	enum Rocks {
		CLAY, TIN, COPPER, IRON, COAL, SILVER, GOLD, MITH, ADDY, RUNE, PURE_ESSENCE, GEM, GRANITE
	}

	enum LRCRocks {
		COALSPOT1, COALSPOT2, COALSPOT3, NORTHGOLD, SOUTHGOLD, SOUTHWESTGOLD;
	}

	enum Food {
		TROUT, SALMON, MONKFISH, SHARK, PIKE, LOBSTER, SWORDFISH;
	}
	
	// *****************************************************************************************************************
		/* Locations */
		// *****************************************************************************************************************
		// *****************************************************************************************************************
		/* Dwarf Mines */
		// *****************************************************************************************************************
		public final Area dwarfMineArea = new Area(new Tile(3000, 9786, 0), new Tile(3090, 9783, 0), new Tile(3090, 9758, 0), new Tile(3000, 9758, 0));
		
		// *****************************************************************************************************************
		/* Heroes Guild */
		// *****************************************************************************************************************
		public final Area heroesBankArea = new Area(new Tile[] {
				new Tile(2887, 3535, 0), new Tile(2889, 3524, 0),
				new Tile(2896, 3524, 0), new Tile(2897, 3533, 0)
		});
		
		public final Tile[] heroesBankToDoor = {
				new Tile(2892, 3532, 0), new Tile(2895, 3528, 0),
				new Tile(2896, 3523, 0), new Tile(2896, 3518, 0),
				new Tile(2898, 3513, 0), new Tile(2900, 3508, 0),
				new Tile(2905, 3508, 0), new Tile(2910, 3507, 0),
				new Tile(2915, 3507, 0), new Tile(2919, 3510, 0),
				new Tile(2917, 3513, 0)
		};
		
		public final Tile[] heroesLadderToInnerMine = {
				new Tile(2905, 9912, 0), new Tile(2915, 9913, 0)
		};
		
		public final Area heroesArea = new Area(new Tile(2914, 9925, 0), new Tile(2966, 9925, 0),
				new Tile(2966, 9883, 0), new Tile(2914, 9871, 0));
		
		public final Area downstairsHeroesArea = new Area(new Tile(2867,9927, 0), new Tile(2966, 9925, 0),
				new Tile(2867, 9902, 0), new Tile(2914, 9871, 0));
		
		public final Area heroesGuild = new Area(new Tile[] {
				new Tile(2908, 3516, 0), new Tile(2916, 3515, 0),
				new Tile(2919, 3508, 0), new Tile(2916, 3512, 0)
		});
		
		public final Tile[] innerGuildToLadder = new Tile[] {
			new Tile(2935, 9890, 0), new Tile(2933, 9899, 0), new Tile(2929, 9910, 0), new Tile(2916, 9913, 0), 
			new Tile(2900, 9912, 0), new Tile(2893, 9907, 0)
		};
		
		public final Area outsideHeroes = new Area(new Tile[] {
			new Tile(2917, 3516, 0), new Tile(2919, 3516, 0), new Tile(2919, 3512, 0), new Tile(2917, 3513, 0)	
		});
		// *****************************************************************************************************************
		/* Granite Area */
		// *****************************************************************************************************************
		public final Tile[] quarryTravelToMine = {
				new Tile(3180, 3039, 0), new Tile(3179, 3034, 0),
				new Tile(3180, 3029, 0), new Tile(3182, 3024, 0),
				new Tile(3183, 3019, 0), new Tile(3184, 3014, 0),
				new Tile(3185, 3009, 0), new Tile(3186, 3004, 0),
				new Tile(3187, 2999, 0), new Tile(3190, 2995, 0),
				new Tile(3190, 2990, 0), new Tile(3191, 2985, 0),
				new Tile(3192, 2980, 0), new Tile(3194, 2975, 0),
				new Tile(3197, 2971, 0), new Tile(3198, 2966, 0),
				new Tile(3202, 2962, 0), new Tile(3204, 2957, 0),
				new Tile(3207, 2953, 0), new Tile(3210, 2949, 0),
				new Tile(3211, 2944, 0), new Tile(3212, 2939, 0),
				new Tile(3209, 2935, 0), new Tile(3206, 2931, 0),
				new Tile(3202, 2928, 0), new Tile(3199, 2923, 0),
				new Tile(3197, 2918, 0), new Tile(3193, 2915, 0),
				new Tile(3188, 2913, 0), new Tile(3183, 2914, 0),
				new Tile(3178, 2914, 0), new Tile(3175, 2910, 0),
				new Tile(3176, 2907, 0) 
		};
		
		public final Area graniteBankArea = new Area(new Tile[] {
				new Tile(3297, 3126, 0), new Tile(3312, 3125, 0),
				new Tile(3313, 3116, 0), new Tile(3297, 3117, 0)
		});
		
		public final Area graniteArea = new Area(new Tile[] {
				new Tile(3161, 2920, 0), new Tile(3185, 2924, 0),
				new Tile(3186, 2897, 0), new Tile(3159, 2892, 0)
		});
		
		public final Area shantayArea = new Area(new Tile[] {
				new Tile(3175, 3047, 0), new Tile(3176, 3035, 0),
				new Tile(3190, 3037, 0), new Tile(3187, 3046, 0)
		});
		
		// *****************************************************************************************************************
		/* Crafting Guild */
		// *****************************************************************************************************************
		public final Tile[] portSarimToCraftGuild = {
				new Tile(3047, 3236, 0), new Tile(3042, 3236, 0),
				new Tile(3037, 3236, 0), new Tile(3032, 3237, 0),
				new Tile(3028, 3240, 0), new Tile(3024, 3243, 0),
				new Tile(3019, 3243, 0), new Tile(3014, 3243, 0),
				new Tile(3009, 3242, 0), new Tile(3005, 3246, 0),
				new Tile(3001, 3250, 0), new Tile(2997, 3253, 0),
				new Tile(2994, 3257, 0), new Tile(2991, 3261, 0),
				new Tile(2988, 3266, 0), new Tile(2985, 3270, 0),
				new Tile(2980, 3271, 0), new Tile(2975, 3270, 0),
				new Tile(2970, 3270, 0), new Tile(2965, 3269, 0),
				new Tile(2960, 3269, 0), new Tile(2955, 3270, 0),
				new Tile(2951, 3273, 0), new Tile(2949, 3278, 0),
				new Tile(2947, 3283, 0), new Tile(2946, 3288, 0),
				new Tile(2945, 3293, 0), new Tile(2941, 3296, 0),
				new Tile(2936, 3298, 0), new Tile(2933, 3294, 0),
				new Tile(2934, 3291, 0) 
		};
		
		public final Area craftGuildArea = new Area(new Tile[] {
				new Tile(2939, 3292, 0), new Tile(2942, 3292, 0),
				new Tile(2944, 3289, 0), new Tile(2944, 3276, 0),
				new Tile(2938, 3275, 0), new Tile(2934, 3277, 0),
				new Tile(2930, 3281, 0), new Tile(2932, 3288, 0),
				new Tile(2927, 3287, 0) 
		});
		
		// *****************************************************************************************************************
		/* Adventure Mode */
		// *****************************************************************************************************************
		public final Tile[] evBankToGE = {
				new Tile(3095, 3483, 0), new Tile(3100, 3484, 0),
				new Tile(3105, 3487, 0), new Tile(3109, 3490, 0),
				new Tile(3112, 3494, 0), new Tile(3116, 3497, 0),
				new Tile(3120, 3500, 0), new Tile(3123, 3504, 0),
				new Tile(3126, 3508, 0), new Tile(3131, 3510, 0),
				new Tile(3128, 3514, 0), new Tile(3133, 3516, 0),
				new Tile(3136, 3512, 0), new Tile(3135, 3507, 0),
				new Tile(3133, 3502, 0), new Tile(3133, 3497, 0),
				new Tile(3133, 3492, 0), new Tile(3133, 3487, 0),
				new Tile(3134, 3482, 0), new Tile(3136, 3477, 0),
				new Tile(3137, 3472, 0), new Tile(3139, 3467, 0),
				new Tile(3144, 3465, 0), new Tile(3148, 3462, 0),
				new Tile(3153, 3461, 0), new Tile(3158, 3460, 0),
				new Tile(3163, 3459, 0), new Tile(3163, 3464, 0),
				new Tile(3160, 3468, 0), new Tile(3157, 3472, 0),
				new Tile(3153, 3475, 0), new Tile(3151, 3478, 0)
		};
		
		public final Tile[] walkToSouthBarb = {
				new Tile(3233, 3222, 0), new Tile(3232, 3227, 0),
				new Tile(3229, 3231, 0), new Tile(3225, 3234, 0),
				new Tile(3222, 3238, 0), new Tile(3220, 3243, 0),
				new Tile(3220, 3248, 0), new Tile(3218, 3253, 0),
				new Tile(3216, 3258, 0), new Tile(3216, 3263, 0),
				new Tile(3215, 3268, 0), new Tile(3214, 3273, 0),
				new Tile(3212, 3278, 0), new Tile(3207, 3279, 0),
				new Tile(3202, 3279, 0), new Tile(3197, 3279, 0),
				new Tile(3192, 3281, 0), new Tile(3188, 3284, 0),
				new Tile(3187, 3289, 0), new Tile(3187, 3294, 0),
				new Tile(3187, 3299, 0), new Tile(3187, 3304, 0),
				new Tile(3183, 3308, 0), new Tile(3180, 3312, 0),
				new Tile(3177, 3316, 0), new Tile(3176, 3321, 0),
				new Tile(3175, 3326, 0), new Tile(3176, 3332, 0),
				new Tile(3177, 3337, 0), new Tile(3178, 3342, 0),
				new Tile(3177, 3347, 0), new Tile(3177, 3352, 0),
				new Tile(3177, 3357, 0), new Tile(3174, 3361, 0),
				new Tile(3170, 3364, 0), new Tile(3166, 3367, 0),
				new Tile(3163, 3372, 0), new Tile(3160, 3376, 0),
				new Tile(3158, 3379, 0), new Tile(3156, 3384, 0),
				new Tile(3154, 3389, 0), new Tile(3151, 3395, 0),
				new Tile(3149, 3400, 0), new Tile(3145, 3403, 0),
				new Tile(3140, 3404, 0), new Tile(3135, 3406, 0),
				new Tile(3131, 3409, 0), new Tile(3126, 3412, 0),
				new Tile(3120, 3415, 0), new Tile(3115, 3419, 0),
				new Tile(3111, 3422, 0), new Tile(3106, 3421, 0),
				new Tile(3101, 3421, 0), new Tile(3099, 3416, 0),
				new Tile(3096, 3412, 0), new Tile(3093, 3408, 0),
				new Tile(3090, 3404, 0), new Tile(3085, 3403, 0),
				new Tile(3082, 3399, 0), new Tile(3081, 3398, 0)
		};
		
		public final Tile[] vWestBankToGE = {
				new Tile(3186, 3440, 0), new Tile(3186, 3445, 0),
				new Tile(3186, 3450, 0), new Tile(3181, 3452, 0),
				new Tile(3176, 3453, 0), new Tile(3171, 3454, 0),
				new Tile(3166, 3454, 0), new Tile(3165, 3459, 0),
				new Tile(3164, 3464, 0), new Tile(3163, 3469, 0),
				new Tile(3159, 3472, 0), new Tile(3154, 3474, 0),
				new Tile(3149, 3476, 0) 
		};
		
		public final Area GEArea = new Area(new Tile[] {
				new Tile(3143, 3484, 0), new Tile(3142, 3472, 0),
				new Tile(3155, 3471, 0), new Tile(3156, 3483, 0)
		});
		
		public final Area lodestoneArea = new Area(new Tile[] {
				new Tile(3227, 3219, 0), new Tile(3233, 3227, 0),
				new Tile(3239, 3218, 0), new Tile(3234, 3212, 0) });

		public final Tile[] lodestoneToBobsAxes = { new Tile(3233, 3217, 0),
				new Tile(3234, 3212, 0), new Tile(3235, 3207, 0),
				new Tile(3235, 3202, 0), new Tile(3230, 3202, 0) };

		public final Area lumbridgeArea = new Area(new Tile[] {
				new Tile(3225, 3233, 0), new Tile(3239, 3235, 0),
				new Tile(3238, 3195, 0), new Tile(3225, 3195, 0) });

		public final Area manArea = new Area(new Tile[] { new Tile(3227, 3235, 0),
				new Tile(3243, 3237, 0), new Tile(3248, 3204, 0),
				new Tile(3221, 3204, 0) });

		public final Tile[] bobsAxesToVarrockWest = {
				new Tile(3234, 3218, 0), new Tile(3234, 3223, 0),
				new Tile(3232, 3228, 0), new Tile(3229, 3232, 0),
				new Tile(3226, 3236, 0), new Tile(3223, 3240, 0),
				new Tile(3221, 3245, 0), new Tile(3219, 3250, 0),
				new Tile(3217, 3255, 0), new Tile(3216, 3260, 0),
				new Tile(3215, 3265, 0), new Tile(3215, 3270, 0),
				new Tile(3213, 3275, 0), new Tile(3212, 3280, 0),
				new Tile(3207, 3280, 0), new Tile(3202, 3281, 0),
				new Tile(3197, 3281, 0), new Tile(3192, 3282, 0),
				new Tile(3190, 3287, 0), new Tile(3189, 3292, 0),
				new Tile(3188, 3297, 0), new Tile(3187, 3302, 0),
				new Tile(3184, 3306, 0), new Tile(3181, 3310, 0),
				new Tile(3177, 3313, 0), new Tile(3175, 3318, 0),
				new Tile(3175, 3323, 0), new Tile(3175, 3328, 0),
				new Tile(3175, 3333, 0), new Tile(3176, 3338, 0),
				new Tile(3176, 3343, 0), new Tile(3176, 3348, 0),
				new Tile(3176, 3353, 0), new Tile(3176, 3358, 0),
				new Tile(3176, 3363, 0), new Tile(3178, 3368, 0),
				new Tile(3182, 3375, 0) 
		};

		// *****************************************************************************************************************
		/* Resource Dungeons */
		// *****************************************************************************************************************
		public final Area miningGuildResDungeonArea = new Area(new Tile[] {
				new Tile(1031, 4521, 0), new Tile(1071, 4524, 0),
				new Tile(1051, 4480, 0), new Tile(1031, 4490, 0) });

		public final Area dwarvenMineResDungeonArea = new Area(new Tile[] {
				new Tile(1000, 4600, 0), new Tile(1100, 4600, 0),
				new Tile(1100, 4400, 0), new Tile(1000, 4400, 0) });

		public final Tile[] sfBankToStairs = {
				new Tile(3014, 3356, 0), new Tile(3014, 3361, 0),
				new Tile(3019, 3362, 0), new Tile(3024, 3362, 0),
				new Tile(3029, 3363, 0), new Tile(3032, 3367, 0),
				new Tile(3037, 3368, 0), new Tile(3042, 3369, 0),
				new Tile(3047, 3369, 0), new Tile(3052, 3370, 0),
				new Tile(3057, 3370, 0), new Tile(3060, 3374, 0),
				new Tile(3061, 3378, 0) 
		};

		public final Tile[] stairsToDung = { new Tile(3048, 9779, 0),
				new Tile(3039, 9778, 0), new Tile(3034, 9772, 0) };

		public final Area alKharidResourceDungeonArea = new Area(new Tile[] {
				new Tile(1083, 4612, 0), new Tile(1283, 4612, 0),
				new Tile(1283, 4412, 0), new Tile(1083, 4412, 0) });
		// *****************************************************************************************************************
		/* DeathWalk paths */
		// *****************************************************************************************************************

		public final Tile[] deathWalkFaladorToFaladorEast = {
				new Tile(2969, 3341, 0), new Tile(2966, 3345, 0),
				new Tile(2965, 3350, 0), new Tile(2964, 3355, 0),
				new Tile(2964, 3360, 0), new Tile(2965, 3365, 0),
				new Tile(2970, 3366, 0), new Tile(2975, 3366, 0),
				new Tile(2980, 3365, 0), new Tile(2985, 3363, 0),
				new Tile(2990, 3362, 0), new Tile(2995, 3361, 0),
				new Tile(3000, 3360, 0), new Tile(3006, 3360, 0),
				new Tile(3012, 3362, 0), new Tile(3015, 3358, 0),
				new Tile(3016, 3356, 0) };

		public final Tile[] deathWalkEdgeToEdge = { new Tile(3102, 3497, 0),
				new Tile(3097, 3497, 0) };

		// *****************************************************************************************************************
		/* LRC Paths */
		// *****************************************************************************************************************
		private final static Area LRCMiningArea = new Area(new Tile[] {
				new Tile(3638, 5117, 0), new Tile(3657, 5109, 0), new Tile(3682, 5102, 0), new Tile(3632, 5050, 0), new Tile(3635, 5050, 0)
		});

		private final static Area LRCBankArea = new Area(new Tile[] {
				new Tile(3647, 5116, 0), new Tile(3657, 5116, 0),
				new Tile(3647, 5112, 0), new Tile(3657, 5112, 0) });

		private static final Tile[] LRCBankToCenter = new Tile[] {
			new Tile(3657, 5107, 0), new Tile(3638, 5093, 0)
		};
		// *****************************************************************************************************************
		/* MageGuild essence */
		// *****************************************************************************************************************
		public final Tile[] yanilleBankToMageGuild = { new Tile(2611, 3093, 0),
				new Tile(2606, 3092, 0), new Tile(2603, 3088, 0),
				new Tile(2598, 3088, 0) };
		// *****************************************************************************************************************
		/* Barbarian Village */
		// *****************************************************************************************************************
		public final Tile[] barbarianBankToMine = {
				new Tile(3093, 3492, 0), new Tile(3088, 3489, 0),
				new Tile(3084, 3486, 0), new Tile(3081, 3482, 0),
				new Tile(3080, 3477, 0), new Tile(3078, 3472, 0),
				new Tile(3079, 3467, 0), new Tile(3084, 3465, 0),
				new Tile(3087, 3461, 0), new Tile(3090, 3457, 0),
				new Tile(3091, 3452, 0), new Tile(3092, 3447, 0),
				new Tile(3095, 3443, 0), new Tile(3098, 3439, 0),
				new Tile(3101, 3435, 0), new Tile(3102, 3430, 0),
				new Tile(3102, 3425, 0), new Tile(3102, 3420, 0),
				new Tile(3098, 3417, 0), new Tile(3096, 3412, 0),
				new Tile(3092, 3409, 0), new Tile(3090, 3404, 0),
				new Tile(3086, 3401, 0), new Tile(3083, 3400, 0)
		};

		public final Area barbarianMiningArea = new Area(new Tile[] {
				new Tile(3076, 3425, 0), new Tile(3073, 3391, 0),
				new Tile(3088, 3389, 0), new Tile(3090, 3425, 0)
		});

		// *****************************************************************************************************************
		/* VarrockWest Bank to Mine */
		// *****************************************************************************************************************
		public final Tile[] vWestToMine = {
				new Tile(3182, 3436, 0), new Tile(3179, 3428, 0), new Tile(3171, 3420, 0), 
				new Tile(3171, 3409, 0), new Tile(3173, 3396, 0), 
				new Tile(3179, 3382, 0), new Tile(3182, 3369, 0)
		};

		public final Area vWestArea = new Area(new Tile[] {
				new Tile(3165, 3381, 0), new Tile(3186, 3385, 0),
				new Tile(3188, 3365, 0), new Tile(3172, 3361, 0)
		});

		public final static Area vWestBankArea = new Area(new Tile[] {
				new Tile(3178, 3447, 0), new Tile(3195, 3445, 0),
				new Tile(3195, 3430, 0), new Tile(3178, 3429, 0)
		});

		// *****************************************************************************************************************
		/* VarrockEast paths */
		// *****************************************************************************************************************
		public final Tile[] varrockEastBankToMine = new Tile[] {
				new Tile(3253, 3420, 0), new Tile(3254, 3428, 0),
				new Tile(3259, 3428, 0), new Tile(3267, 3427, 0),
				new Tile(3274, 3427, 0), new Tile(3279, 3428, 0),
				new Tile(3284, 3425, 0), new Tile(3287, 3415, 0),
				new Tile(3289, 3410, 0), new Tile(3290, 3399, 0),
				new Tile(3293, 3387, 0), new Tile(3292, 3382, 0),
				new Tile(3293, 3374, 0), new Tile(3287, 3370, 0),
				new Tile(3286, 3367, 0) };

		public final Tile[] varrockEastBankToMine1 = new Tile[] {
				new Tile(3255, 3421, 0), new Tile(3254, 3424, 0),
				new Tile(3252, 3431, 0), new Tile(3260, 3427, 0),
				new Tile(3265, 3427, 0), new Tile(3272, 3428, 0),
				new Tile(3275, 3428, 0), new Tile(3284, 3423, 0),
				new Tile(3286, 3415, 0), new Tile(3290, 3408, 0),
				new Tile(3292, 3398, 0), new Tile(3293, 3386, 0),
				new Tile(3292, 3381, 0), new Tile(3289, 3373, 0),
				new Tile(3286, 3370, 0) };

		public final Area vEastArea = new Area(new Tile[] {
				new Tile(3280, 3371, 0), new Tile(3293, 3372, 0),
				new Tile(3293, 3363, 0), new Tile(3281, 3359, 0) });

		public final static Area vEastBankArea = new Area(new Tile[] {
				new Tile(3249, 3424, 0), new Tile(3258, 3425, 0),
				new Tile(3259, 3418, 0), new Tile(3250, 3415, 0) });

		public final Tile[][] varrockEastBankToMinePaths = new Tile[][] {
				varrockEastBankToMine, varrockEastBankToMine1 };
		// *****************************************************************************************************************
		/* Al kharid paths */
		// *****************************************************************************************************************
		public final Tile[] alKharidBankToMine = { new Tile(3271, 3166, 0),
				new Tile(3275, 3169, 0), new Tile(3277, 3174, 0),
				new Tile(3277, 3179, 0), new Tile(3280, 3183, 0),
				new Tile(3284, 3186, 0), new Tile(3284, 3191, 0),
				new Tile(3283, 3196, 0), new Tile(3284, 3201, 0),
				new Tile(3285, 3206, 0), new Tile(3287, 3211, 0),
				new Tile(3291, 3214, 0), new Tile(3294, 3218, 0),
				new Tile(3297, 3222, 0), new Tile(3298, 3227, 0),
				new Tile(3298, 3232, 0), new Tile(3299, 3237, 0),
				new Tile(3299, 3242, 0), new Tile(3298, 3247, 0),
				new Tile(3297, 3252, 0), new Tile(3297, 3257, 0),
				new Tile(3296, 3262, 0), new Tile(3296, 3267, 0),
				new Tile(3296, 3272, 0), new Tile(3297, 3277, 0),
				new Tile(3299, 3282, 0), new Tile(3301, 3287, 0),
				new Tile(3300, 3292, 0), new Tile(3300, 3297, 0),
				new Tile(3299, 3302, 0) };

		public final static Area akBankArea = new Area(new Tile[] {
				new Tile(3269, 3175, 0), new Tile(3273, 3174, 0),
				new Tile(3273, 3159, 0), new Tile(3268, 3158, 0) });

		public final Area akArea = new Area(new Tile[] { new Tile(3294, 3288, 0),
				new Tile(3307, 3292, 0), new Tile(3305, 3319, 0),
				new Tile(3295, 3318, 0) });

		// *****************************************************************************************************************
		/* South lumby paths */
		// *****************************************************************************************************************
		public final Tile[] southLumbyBankToMine = { new Tile(3094, 3244, 0),
				new Tile(3093, 3248, 0), new Tile(3098, 3248, 0),
				new Tile(3103, 3249, 0), new Tile(3105, 3244, 0),
				new Tile(3106, 3239, 0), new Tile(3108, 3234, 0),
				new Tile(3109, 3229, 0), new Tile(3112, 3225, 0),
				new Tile(3115, 3221, 0), new Tile(3118, 3217, 0),
				new Tile(3121, 3213, 0), new Tile(3124, 3209, 0),
				new Tile(3128, 3206, 0), new Tile(3133, 3205, 0),
				new Tile(3136, 3201, 0), new Tile(3138, 3196, 0),
				new Tile(3140, 3191, 0), new Tile(3142, 3186, 0),
				new Tile(3143, 3181, 0), new Tile(3145, 3176, 0),
				new Tile(3146, 3171, 0), new Tile(3147, 3166, 0),
				new Tile(3147, 3161, 0), new Tile(3148, 3156, 0),
				new Tile(3148, 3151, 0), new Tile(3146, 3146, 0) };

		public final Tile[] southLumbyBankToMine1 = { new Tile(3092, 3249, 0),
				new Tile(3097, 3248, 0), new Tile(3102, 3250, 0),
				new Tile(3106, 3247, 0), new Tile(3106, 3242, 0),
				new Tile(3107, 3237, 0), new Tile(3108, 3232, 0),
				new Tile(3110, 3227, 0), new Tile(3113, 3222, 0),
				new Tile(3117, 3219, 0), new Tile(3120, 3215, 0),
				new Tile(3123, 3211, 0), new Tile(3126, 3207, 0),
				new Tile(3131, 3205, 0), new Tile(3134, 3201, 0),
				new Tile(3136, 3196, 0), new Tile(3138, 3191, 0),
				new Tile(3140, 3186, 0), new Tile(3141, 3181, 0),
				new Tile(3142, 3176, 0), new Tile(3144, 3171, 0),
				new Tile(3146, 3166, 0), new Tile(3147, 3161, 0),
				new Tile(3148, 3156, 0), new Tile(3148, 3151, 0),
				new Tile(3147, 3146, 0) };

		public final Area sLArea = new Area(new Tile[] { new Tile(3154, 3141, 0),
				new Tile(3150, 3156, 0), new Tile(3141, 3152, 0),
				new Tile(3140, 3141, 0) });

		public final static Area sLBankArea = new Area(new Tile[] {
				new Tile(3088, 3240, 0), new Tile(3088, 3250, 0),
				new Tile(3100, 3248, 0), new Tile(3101, 3241, 0) });

		public final Tile[][] southLumbyToMinePaths = new Tile[][] {
				southLumbyBankToMine, southLumbyBankToMine1 };
		// *****************************************************************************************************************
		/* Wilderness Castle Rocks */
		// *****************************************************************************************************************
		public final Tile[] edgeBankToDitch = { new Tile(3099, 3500, 0),
				new Tile(3101, 3511, 0), new Tile(3103, 3519, 0) };

		public final Area edgeDitchPitStop = new Area(new Tile[] {
				new Tile(3112, 3517, 0), new Tile(3093, 3516, 0),
				new Tile(3092, 3524, 0), new Tile(3110, 3526, 0) });

		public final static Area crArea = new Area(new Tile[] {
				new Tile(3026, 3585, 0), new Tile(3022, 3598, 0),
				new Tile(3009, 3596, 0), new Tile(3008, 3585, 0) });

		// *****************************************************************************************************************
		/* Wilderness Rune Rocks */
		// *****************************************************************************************************************
		public final Tile[] runeRockPath = {
				new Tile(3097, 3522, 0), new Tile(3099, 3527, 0),
				new Tile(3100, 3532, 0), new Tile(3100, 3537, 0),
				new Tile(3100, 3542, 0), new Tile(3100, 3547, 0),
				new Tile(3100, 3552, 0), new Tile(3101, 3557, 0),
				new Tile(3103, 3562, 0), new Tile(3101, 3567, 0),
				new Tile(3100, 3572, 0), new Tile(3098, 3577, 0),
				new Tile(3097, 3582, 0), new Tile(3096, 3587, 0),
				new Tile(3096, 3592, 0), new Tile(3096, 3597, 0),
				new Tile(3098, 3602, 0), new Tile(3101, 3607, 0),
				new Tile(3099, 3612, 0), new Tile(3096, 3617, 0),
				new Tile(3095, 3622, 0), new Tile(3095, 3627, 0),
				new Tile(3095, 3632, 0), new Tile(3095, 3637, 0),
				new Tile(3094, 3642, 0), new Tile(3091, 3646, 0),
				new Tile(3087, 3649, 0), new Tile(3084, 3653, 0),
				new Tile(3080, 3656, 0), new Tile(3076, 3659, 0),
				new Tile(3073, 3663, 0), new Tile(3072, 3668, 0),
				new Tile(3073, 3673, 0), new Tile(3072, 3678, 0),
				new Tile(3070, 3683, 0), new Tile(3071, 3688, 0),
				new Tile(3073, 3693, 0), new Tile(3075, 3699, 0),
				new Tile(3076, 3704, 0), new Tile(3076, 3709, 0),
				new Tile(3076, 3714, 0), new Tile(3076, 3719, 0),
				new Tile(3076, 3724, 0), new Tile(3076, 3730, 0),
				new Tile(3077, 3737, 0), new Tile(3080, 3740, 0),
				new Tile(3082, 3745, 0), new Tile(3085, 3749, 0),
				new Tile(3090, 3752, 0), new Tile(3094, 3755, 0),
				new Tile(3098, 3758, 0), new Tile(3101, 3762, 0),
				new Tile(3104, 3766, 0), new Tile(3107, 3771, 0),
				new Tile(3108, 3776, 0), new Tile(3110, 3781, 0),
				new Tile(3111, 3786, 0), new Tile(3112, 3791, 0),
				new Tile(3113, 3796, 0), new Tile(3116, 3801, 0),
				new Tile(3117, 3806, 0), new Tile(3118, 3811, 0),
				new Tile(3120, 3816, 0), new Tile(3121, 3822, 0),
				new Tile(3123, 3828, 0), new Tile(3123, 3833, 0),
				new Tile(3123, 3838, 0), new Tile(3123, 3843, 0),
				new Tile(3123, 3848, 0), new Tile(3123, 3853, 0),
				new Tile(3123, 3858, 0), new Tile(3123, 3863, 0),
				new Tile(3123, 3869, 0), new Tile(3121, 3875, 0),
				new Tile(3117, 3879, 0), new Tile(3114, 3883, 0),
				new Tile(3110, 3886, 0), new Tile(3105, 3887, 0),
				new Tile(3100, 3887, 0), new Tile(3094, 3885, 0),
				new Tile(3088, 3885, 0), new Tile(3083, 3885, 0),
				new Tile(3078, 3885, 0), new Tile(3073, 3885, 0),
				new Tile(3067, 3884, 0), new Tile(3062, 3884, 0),
				new Tile(3061, 3884, 0) 
		};

		public final Tile[] ditchToRestStop = { new Tile(3095, 3527, 0),
				new Tile(3091, 3530, 0), new Tile(3086, 3532, 0),
				new Tile(3081, 3533, 0), new Tile(3076, 3535, 0),
				new Tile(3071, 3536, 0), new Tile(3066, 3538, 0),
				new Tile(3061, 3539, 0), new Tile(3056, 3541, 0),
				new Tile(3052, 3544, 0), new Tile(3047, 3546, 0),
				new Tile(3043, 3550, 0), new Tile(3039, 3553, 0),
				new Tile(3036, 3557, 0), new Tile(3033, 3562, 0),
				new Tile(3031, 3567, 0), new Tile(3029, 3572, 0),
				new Tile(3027, 3577, 0), new Tile(3026, 3582, 0),
				new Tile(3026, 3587, 0), new Tile(3025, 3592, 0),
				new Tile(3022, 3597, 0), new Tile(3018, 3600, 0),
				new Tile(3015, 3605, 0), new Tile(3014, 3610, 0),
				new Tile(3011, 3614, 0), new Tile(3008, 3618, 0),
				new Tile(3004, 3622, 0), new Tile(3001, 3626, 0),
				new Tile(3000, 3631, 0), new Tile(3000, 3636, 0),
				new Tile(3002, 3641, 0), new Tile(3006, 3646, 0),
				new Tile(3010, 3650, 0), new Tile(3011, 3655, 0),
				new Tile(3011, 3662, 0), new Tile(3009, 3668, 0),
				new Tile(3008, 3673, 0), new Tile(3006, 3678, 0),
				new Tile(3006, 3684, 0), new Tile(3006, 3689, 0),
				new Tile(3006, 3694, 0), new Tile(3007, 3699, 0),
				new Tile(3005, 3704, 0), new Tile(3007, 3710, 0),
				new Tile(3006, 3716, 0), new Tile(3003, 3721, 0),
				new Tile(3001, 3727, 0), new Tile(3001, 3732, 0),
				new Tile(3002, 3737, 0) };

		public final Tile[] restStopToRuneRocks = { new Tile(3015, 3743, 0),
				new Tile(3019, 3746, 0), new Tile(3022, 3750, 0),
				new Tile(3025, 3754, 0), new Tile(3028, 3758, 0),
				new Tile(3031, 3762, 0), new Tile(3034, 3766, 0),
				new Tile(3036, 3771, 0), new Tile(3037, 3776, 0),
				new Tile(3037, 3782, 0), new Tile(3037, 3787, 0),
				new Tile(3037, 3792, 0), new Tile(3036, 3797, 0),
				new Tile(3034, 3802, 0), new Tile(3033, 3807, 0),
				new Tile(3031, 3812, 0), new Tile(3030, 3817, 0),
				new Tile(3029, 3822, 0), new Tile(3027, 3827, 0),
				new Tile(3026, 3832, 0), new Tile(3022, 3835, 0),
				new Tile(3017, 3836, 0), new Tile(3012, 3838, 0),
				new Tile(3007, 3840, 0), new Tile(3005, 3845, 0),
				new Tile(3006, 3850, 0), new Tile(3008, 3855, 0),
				new Tile(3008, 3860, 0), new Tile(3011, 3864, 0),
				new Tile(3016, 3865, 0), new Tile(3021, 3866, 0),
				new Tile(3026, 3866, 0), new Tile(3031, 3866, 0),
				new Tile(3036, 3866, 0), new Tile(3041, 3868, 0),
				new Tile(3044, 3872, 0), new Tile(3047, 3876, 0),
				new Tile(3051, 3880, 0), new Tile(3056, 3882, 0),
				new Tile(3062, 3883, 0) };

		public final Area restStopArea = new Area(new Tile[] {
				new Tile(3015, 3734, 0), new Tile(3008, 3755, 0),
				new Tile(2992, 3745, 0), new Tile(2997, 3729, 0) });

		public final static Area evBankArea = new Area(new Tile[] {
				new Tile(3090, 3487, 0), new Tile(3098, 3486, 0),
				new Tile(3099, 3499, 0), new Tile(3091, 3499, 0) });

		public final static Area runeArea = new Area(new Tile[] {
				new Tile(3070, 3890, 0), new Tile(3067, 3873, 0),
				new Tile(3048, 3875, 0), new Tile(3052, 3889, 0) });

		public final Area loginArea = new Area(new Tile[] {
				new Tile(3066, 3875, 0), new Tile(3069, 3887, 0),
				new Tile(3078, 3886, 0), new Tile(3080, 3875, 0) });
		// *****************************************************************************************************************
		/* South Falador */
		// *****************************************************************************************************************
		public final Tile[] rimmingtonToMine = {
				new Tile(3048, 3236, 0), new Tile(3043, 3235, 0),
				new Tile(3038, 3234, 0), new Tile(3033, 3235, 0),
				new Tile(3029, 3238, 0), new Tile(3026, 3242, 0),
				new Tile(3021, 3243, 0), new Tile(3016, 3243, 0),
				new Tile(3011, 3242, 0), new Tile(3006, 3243, 0),
				new Tile(3001, 3245, 0), new Tile(2996, 3248, 0),
				new Tile(2992, 3251, 0), new Tile(2987, 3252, 0),
				new Tile(2982, 3254, 0), new Tile(2977, 3255, 0),
				new Tile(2976, 3250, 0), new Tile(2976, 3245, 0),
				new Tile(2979, 3241, 0), new Tile(2975, 3239, 0)
		};
		
		public final Area rimmingtonBankArea = new Area(new Tile[] {
				new Tile(3040, 3239, 0), new Tile(3041, 3230, 0),
				new Tile(3054, 3229, 0), new Tile(3049, 3240, 0)
		});
		
		public final static Area sfBankArea = new Area(new Tile[] {
				new Tile(3008, 3351, 0), new Tile(3021, 3352, 0),
				new Tile(3022, 3359, 0), new Tile(3006, 3359, 0) });

		public final Area sfArea = new Area(new Tile[] { new Tile(2959, 3252, 0),
				new Tile(2994, 3253, 0), new Tile(2996, 3222, 0),
				new Tile(2962, 3220, 0) });
		// *****************************************************************************************************************
		/* Mining guild */
		// *****************************************************************************************************************
		public final Tile[] faladorBankToGuild = new Tile[] {
				new Tile(3015, 3357, 0), new Tile(3014, 3361, 0),
				new Tile(3022, 3359, 0), new Tile(3024, 3352, 0),
				new Tile(3031, 3345, 0), new Tile(3031, 3335, 0),
				new Tile(3021, 3338, 0) };

		public final Tile[] innerGuildToRocks = new Tile[] {
				new Tile(3023, 9739, 0), new Tile(3030, 9736, 0),
				new Tile(3036, 9737, 0), new Tile(3044, 9735, 0) };

		public final Area miningGuildArea = new Area(new Tile(3026, 9751, 0),
				new Tile(3062, 9726, 0));

		public final Tile[][] faladorBankToGuildPaths = new Tile[][] { faladorBankToGuild };
		// *****************************************************************************************************************
		/* Yanille Mining/Port Khazard */
		// *****************************************************************************************************************
		public final Tile[] yanilleBankToMine = { new Tile(2612, 3091, 0),
				new Tile(2607, 3091, 0), new Tile(2606, 3096, 0),
				new Tile(2606, 3101, 0), new Tile(2610, 3104, 0),
				new Tile(2615, 3106, 0), new Tile(2618, 3110, 0),
				new Tile(2620, 3115, 0), new Tile(2623, 3119, 0),
				new Tile(2627, 3122, 0), new Tile(2630, 3126, 0),
				new Tile(2632, 3131, 0), new Tile(2632, 3136, 0),
				new Tile(2631, 3141, 0), new Tile(2630, 3146, 0) };

		public final static Area yanilleBankArea = new Area(new Tile[] {
				new Tile(2617, 3087, 0), new Tile(2607, 3087, 0),
				new Tile(2608, 3098, 0), new Tile(2617, 3097, 0) });

		public final Area yanilleMiningArea = new Area(new Tile[] {
				new Tile(2642, 3134, 0), new Tile(2639, 3153, 0),
				new Tile(2620, 3156, 0), new Tile(2620, 3134, 0) });

		// *****************************************************************************************************************
		/* Ardougne Mining */
		// *****************************************************************************************************************
		public final Tile[] ardougneBankToMine = { new Tile(2651, 3284, 0),
				new Tile(2646, 3283, 0), new Tile(2641, 3285, 0),
				new Tile(2644, 3289, 0), new Tile(2647, 3293, 0),
				new Tile(2651, 3296, 0), new Tile(2655, 3299, 0),
				new Tile(2660, 3300, 0), new Tile(2664, 3303, 0),
				new Tile(2669, 3305, 0), new Tile(2674, 3305, 0),
				new Tile(2679, 3306, 0), new Tile(2684, 3306, 0),
				new Tile(2689, 3307, 0), new Tile(2694, 3306, 0),
				new Tile(2699, 3308, 0), new Tile(2700, 3313, 0),
				new Tile(2699, 3318, 0), new Tile(2698, 3323, 0),
				new Tile(2697, 3328, 0), new Tile(2700, 3332, 0) };

		public final Area ardougneMiningArea = new Area(new Tile[] {
				new Tile(2720, 3327, 0), new Tile(2688, 3326, 0),
				new Tile(2686, 3343, 0), new Tile(2715, 3342, 0) });

		public final static Area ardougneBankArea = new Area(new Tile[] {
				new Tile(2659, 3278, 0), new Tile(2644, 3278, 0),
				new Tile(2644, 3288, 0), new Tile(2660, 3288, 0) });

		// *****************************************************************************************************************
		/* Essence Mining(Varrock) */
		// *****************************************************************************************************************
		public final Tile[] vEssence = { new Tile(3254, 3422, 0),
				new Tile(3254, 3424, 0), new Tile(3254, 3427, 0),
				new Tile(3257, 3428, 0), new Tile(3262, 3426, 0),
				new Tile(3262, 3421, 0), new Tile(3262, 3417, 0),
				new Tile(3261, 3413, 0), new Tile(3259, 3410, 0),
				new Tile(3258, 3406, 0), new Tile(3257, 3400, 0),
				new Tile(3255, 3397, 0) };

		public final Area vPitStopArea = new Area(new Tile[] {
				new Tile(3264, 3393, 0), new Tile(3249, 3394, 0),
				new Tile(3251, 3408, 0), new Tile(3263, 3407, 0) });

		public final int[] essenceTracks = new int[] { 448, 469, 493, 495, 467,
				455, 468, 454 };

	
	// *****************************************************************************************************************
		/* Filters */
		// *****************************************************************************************************************
		
		private Filter<SceneObject> edgeDitchFilter = new Filter<SceneObject>() {

			@Override
			public boolean accept(SceneObject arg0) {
				return arg0.getId() == 65094;
			}
			
		};
		
		private Filter<Item> waterskins = new Filter<Item>() {

			@Override
			public boolean accept(Item arg0) {
				for (int i = 0; i <waterskinIDs.length; i++) {
					if (arg0.getId() == waterskinIDs[i]) {
						return true;
					}
				}
				return false;
			}
		};
		
		private Filter<Item> strangeRocks = new Filter<Item>() {
			@Override
			public boolean accept(Item arg0) {
				return arg0.getId() > 15522 && arg0.getId() < 15551;
			}
			
		};
		
		
		private Filter<NPC> menKiller = new Filter<NPC>() {
			@Override
			public boolean accept(NPC arg0) {
				return arg0.getId() == 7875 || arg0.getId() == 7883;
			}
		};

		private Filter<SceneObject> secondaryRocks;
		private Filter<SceneObject> selectedRocks;
		private Filter<SceneObject> essenceRock = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject arg0) {
				return (arg0.getId() == 2491);
			}
		};

		private Filter<SceneObject> essenceMineFilter = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject arg0) {
				for (int track : essenceTracks) {
					if (arg0.getId() == track) {
						return true;
					}
				}
				return false;
			}
		};

		private Filter<SceneObject> mageGuildDoor = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject obj) {
				return (Calculations.distance(obj.getLocation(), new Tile(2597,
						3087, 0)) <= 1 && obj.getId() == 1601);
			}
		};

		private Filter<NPC> essencePortals = new Filter<NPC>() {
			@Override
			public boolean accept(NPC arg0) {
				for (int i = 0; i < essencePortalIDs.length; i++) {
					if (arg0.getId() == essencePortalIDs[i]) {
						return true;
					}
				}
				return false;
			}
		};

		private Filter<Item> ignoreableDepositItems = new Filter<Item>() {

			@Override
			public boolean accept(Item arg0) {
				for (int i = 0; i < ScriptSettings.bankItemsToWithdraw.length; i++) {
					if (ScriptSettings.bankItemsToWithdraw[i] != null) {
						if (arg0.getName().equalsIgnoreCase(
								ScriptSettings.bankItemsToWithdraw[i])) {
							return false;
						}
					}
				}
				return true;
			}

		};

		private Filter<SceneObject> emptyRuneRocks = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject s) {
				for (int i = 0; i < emptyRune.length; i++) {
					if (s.getId() == emptyRune[i]) {
						return true;
					}
				}
				return false;
			}
		};

		private Filter<Item> gemFilter = new Filter<Item>() {
			@Override
			public boolean accept(Item k) {
				for (int i = 0; i < gemIDs.length; i++) {
					if (gemIDs[i] == k.getId()) {
						return true;
					}
				}
				return false;
			}
		};

		private Filter<SceneObject> LRCBank = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject s) {
				return s.getId() == 45079;
			}
		};

		private Filter<SceneObject> LRCGoldRock = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject s) {
				return s.getId() == 45076;
			}
		};

		private Filter<SceneObject> LRCCoalRock = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject s) {
				return s.getId() == 5999;
			}
		};

		private Filter<Item> summoningPotions = new Filter<Item>() {
			@Override
			public boolean accept(Item it) {
				for (int i = 0; i < summoningPots.length; i++) {
					if (it.getId() == summoningPots[i]) {
						return true;
					}
				}
				return false;
			}
		};

		private Filter<Item> completeUrn = new Filter<Item>() {
			@Override
			public boolean accept(Item it) {
				for (int i = 0; i < completeUrns.length; i++) {
					if (it.getId() == completeUrns[i]) {
						return true;
					}
				}
				return true;
			}
		};

		private Filter<Integer> idsToIgnore = new Filter<Integer>() {

			@Override
			public boolean accept(Integer arg0) {
				for (int i = 0; i < ScriptSettings.bankItemsToIgnore.length; i++) {
					if (arg0.equals(ScriptSettings.bankItemsToIgnore[i])) {
						return false;
					}
				}
				return true;
			}

		};

		Filter<SceneObject> resourceDungeons = new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject arg0) {
				return (arg0.getId() > 52850 && arg0.getId() < 52875);
			}
		};

		Filter<SceneObject> depoBoxes = new Filter<SceneObject>() {

			@Override
			public boolean accept(SceneObject arg0) {
				return (arg0.getId() == 25937);
			}

		};
	// *****************************************************************************************************************
	/* Widgets */
	// *****************************************************************************************************************
	private boolean guiDone = false;
	Image theKing;
	Image hisLogo;
	Image hisGold;
	Image hisXp;
	Image hisTimer;
	Image hisSmithing;
	Image hisMagic;
	Image hisLocation;
	Long runTimeMs = 0L;
	int startXP;
	int smithingStartXP;
	int magicStartXP;
	int bankTrips = 0;

	static int clay = 434;
	static int iron = 440;
	static int copper = 436;
	static int coal = 453;
	static int silver = 442;
	static int gold = 444;
	static int mith = 447;
	static int addy = 449;
	static int rune = 451;
	static int tin = 438;
	static int price = 0;
	static int sapphire = 1623;
	static int emerald = 1621;
	static int diamond = 1617;
	static int ruby = 1619;
	static int granite = 6979;
	static long ReturnTime[] = new long[139];
	HashSet<Tile> rockBlacklist = new HashSet<Tile>();
	
	static int[] sellBoxes = new int[] { 29, 45, 61, 73, 85, 101};
	static int[] buyBoxes = new int[] { 30, 46, 62, 74, 86, 102 };
	int[] waterskinIDs = new int[] { 1823, 1825, 1827, 1829 };
	int[] graniteIDs= new int[] { 6979, 6981, 6983 };
	int[] emptyRune = new int[] { 14833, 14831, 14832, 14834 };
	int[] noBank = new int[] { 0, 361, 379, 333, 329, 373, 7946, 385, 1273 };
	int[] foodIDs = new int[] { 361, 379, 333, 329, 373, 7946, 385 };
	int[] miningAnimations = new int[] { 625, 6752 };
	int[] essencePortalIDs = new int[] { 13628, 13629, 13630, 13631 };
	int[] pickaxeID = new int[] { 1271, 1265, 15259, 1267, 1273, 1275, 1269 };
	int[] gemIDs = new int[] { sapphire, emerald, ruby, diamond };
	int[] superHeatItemz = new int[] { 561, 444 };
	int[] summoningPots = new int[] { 12140, 12142, 12144, 12146 };
	int[] completeUrns = new int[] { 20396, 20402, 20408, 20288, 20294 };
	int[] incompleteUrns = new int[] { 20394, 20400, 20406, 20286, 20292 };
	String[] bankFoodSearch = new String[] { "Trout", "Salmon", "Lobster",
			"Tuna", "Swordfish" };
	final static JFrame popUpWindow = new JFrame("LRC rock selection");
	final static JPanel panel = new JPanel();

	// *****************************************************************************************************************
		/* Helper Methods and classes */
		// *****************************************************************************************************************
		static class ScriptSettings {
			static Area miningArea = runeArea;
			static Area bankArea;
			static Area pitStopAreas;

			static Locations location;
			static Pickaxes pickaxe;
			static Modes mode;
			static Rocks rock2;
			static Rocks rock;
			static Food food;
			static Bars barToSuperheat = Bars.IRON;
			
			static Area lastLRCArea;
			static Area nextLRCArea;

			static Integer[] rocks = new Integer[] { 2096, 2097, 3032, 3233, 5770,
					5771, 5772, 10948, 11930, 11931, 11932, 14850, 14851, 21287,
					21288, 21289, 29215, 29216, 29217, 32426, 32427, 32428 };
			static Integer[] secondaryRocks;
			
			static Tile[] deathWalkPath;
			static Tile[] toMinePath;
			
			static int rockID;
			static int lastInvSize = 0;
			static int currentInvSize = 0;
			static int oresMined = 0;
			static int rockPrice = 0;
			static int dropDelay = 0;
			static int mineDelay = 0;
			static int rock2ID;
			static int foodID;
			static int foodWithdrawAmount;
			static int totalGold;
			static int castSuperHeatOn = iron;
			
			static int secondaryRequirement = 0;
			static int primaryRequirement = 1;
			
			static int primaryOre = iron;
			static int secondaryOre = iron;
			
			static int[] bankItemsToIgnore = new int[50];

			static String[] bankItemsToWithdraw = new String[30];
			static String status;
			
			static int[] animationIDs = { 624, 625, 626, 627, 628, 629 } ;
			
			static boolean members = false;
			static boolean free = false;
			static boolean highRisk = false;
			static boolean skillWorlds = false;
			static boolean guiDone = false;
			static boolean wearingEnchantedTiara = false;
			static boolean hasPickaxeInInvOrEquip = false;
			static boolean useSuperheat;
			static boolean useLavaTitan;
			static boolean useGoldRocks;
			static boolean useCoalRocks;
			static boolean useMiningUrns;
			static boolean useSmithingUrns;
			static boolean lavaTitanSummoned = false;
			static boolean atSouthRock = false;
			static boolean antiBanOn = false;
			static boolean bankGems = false;
			static boolean rockPrediction = false;
			static boolean rockBlacklisting = false;
			static boolean useDeathWalker;
			static boolean canCastSuperheat;
			public static TilePath reverseToMinePath;
			public static boolean walking;
			
			
			private static final HashMap<String, Integer> bankDatabase = new HashMap<String, Integer>() {
				{
					put("rune pickaxe", 1275);
					put("adamant pickaxe", 1271);
					put("mithril pickaxe", 1273);
					put("steel pickaxe", 1269);
					put("iron pickaxe", 1267);
					put("bronze pickaxe", 1265);
					put("dragon pickaxe", 15259);
					put("inferno adze", 13661);
					
					put("trout", 333);
					put("salmon", 329);
					put("lobster", 379);
					put("swordfish", 373);
					put("pike", 351);
					put("shark", 385);
					put("monkfish", 7946);
					
					put("smelting urn", 20286);
					put("strong smelting urn", 20292);
					put("mining urn", 20395);
					put("strong mining urn", 20400);
					put("decorated mining urn", 20406);
					put("lava titan pouch", 12788);
					put("obsidian golem pouch", 12792);
					put("summoning potion", 12140);
					put("summoning potion", 12142);
					put("summoning potion", 12144);
					put("summoning potion", 12146);
					put("nature rune", 561);
					put("staff of fire", 1387);
					
				}
			};
		}
	
		
		// *****************************************************************************************************************
		/* GUI */
		// *****************************************************************************************************************
		@SuppressWarnings("serial")
		private class SpiffyGUI extends JFrame {

			public SpiffyGUI() {

				setForeground(Color.WHITE);
				setAlwaysOnTop(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setBounds(new Rectangle(450, 300));
				setTitle("KingMinerV2.4");
				JTabbedPane jtp = new JTabbedPane();
				getContentPane().add(jtp);
				JPanel jp2 = new JPanel();
				JPanel jp3 = new JPanel();
				JPanel jp1 = new JPanel();
				JPanel jp4 = new JPanel();
				jtp.addTab("Tab1", jp1);
				jp1.setLayout(null);

				final JCheckBox chckbxAntiban = new JCheckBox("Anti-Ban");
				chckbxAntiban.setBounds(150, 12, 96, 24);
				jp1.add(chckbxAntiban);

				JLabel lblLocation = new JLabel("Location:");
				lblLocation.setBounds(12, 80, 87, 24);
				jp1.add(lblLocation);

				JLabel lblMode = new JLabel("Mode:");
				lblMode.setBounds(12, 132, 70, 18);
				jp1.add(lblMode);

				JLabel lblPickaxe = new JLabel("Pickaxe:");
				lblPickaxe.setBounds(12, 183, 70, 18);
				jp1.add(lblPickaxe);

				final JComboBox location = new JComboBox();
				location.setBounds(12, 104, 180, 22);
				location.setModel(new DefaultComboBoxModel(new Locations[]

				{ Locations.VARROCK_WEST, Locations.VARROCK_EAST,
						Locations.SOUTH_LUMBY, Locations.AL_KHARID,
						Locations.MINING_GUILD, Locations.WILDERNESS_CASTLE,
						Locations.RUNE_ROCKS, Locations.RIMMINGTON,
						Locations.QUARRY, Locations.CRAFT_GUILD,
						Locations.DWARF_MINE, Locations.HERO_GUILD,
						Locations.VARROCK_ESSENCE, Locations.MAGEGUILD_ESSENCE,
						Locations.YANILLE, Locations.ARDOUGNE,
						Locations.BARBARIAN_VILLAGE, Locations.LRC,
						Locations.DWARVEN_MINES_RES_DUNGEON,
						Locations.MINING_GUILD_RES_DUNGEON,
						Locations.ALKHARID_RES_DUNGEON }));
				location.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				jp1.add(location);

				final JComboBox rock2 = new JComboBox();
				rock2.setBounds(10, 57, 133, 24);
				rock2.setModel(new DefaultComboBoxModel(new Rocks[] { Rocks.CLAY,
						Rocks.TIN, Rocks.COPPER, Rocks.IRON, Rocks.COAL,
						Rocks.SILVER, Rocks.GOLD, Rocks.MITH, Rocks.ADDY,
						Rocks.RUNE, Rocks.PURE_ESSENCE, Rocks.GEM, Rocks.GRANITE }));
				jp1.add(rock2);
				rock2.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				final JComboBox rock = new JComboBox();
				rock.setBounds(10, 25, 133, 24);
				rock.setModel(new DefaultComboBoxModel(new Rocks[] { Rocks.CLAY,
						Rocks.TIN, Rocks.COPPER, Rocks.IRON, Rocks.COAL,
						Rocks.SILVER, Rocks.GOLD, Rocks.MITH, Rocks.ADDY,
						Rocks.RUNE, Rocks.PURE_ESSENCE, Rocks.GEM, Rocks.GRANITE }));
				rock.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				jp1.add(rock);
				final JComboBox mode = new JComboBox();
				mode.setBounds(12, 151, 133, 28);
				mode.setModel(new DefaultComboBoxModel(new Modes[]

				{ Modes.BANKING, Modes.POWERMINING }));
				mode.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				jp1.add(mode);

				final JComboBox pickaxe = new JComboBox();
				pickaxe.setBounds(12, 202, 133, 28);
				pickaxe.setModel(new DefaultComboBoxModel(new Pickaxes[]

				{ Pickaxes.BRONZE_PICK, Pickaxes.ADDY_PICK, Pickaxes.MITH_PICK,
						Pickaxes.STEEL_PICK, Pickaxes.IRON_PICK,
						Pickaxes.RUNE_PICK, Pickaxes.INFERNO_ADZE,
						Pickaxes.DRAGON_PICK }));
				pickaxe.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				jp1.add(pickaxe);

				jtp.addTab("General", jp1);
				jp1.setLayout(null);
				jp2.setLayout(null);

				JLabel lblRock = new JLabel("Rocks(2):");
				lblRock.setBounds(12, 10, 70, 18);
				jp1.add(lblRock);

				final JCheckBox lavaTitan = new JCheckBox("Use Lava Titan");
				lavaTitan.setBounds(250, 12, 133, 24);
				jp1.add(lavaTitan);

				final JCheckBox superHeat = new JCheckBox("Use SuperHeat");
				superHeat.setBounds(250, 105, 161, 24);
				jp1.add(superHeat);

				final JCheckBox miningUrns = new JCheckBox("Use Mining Urns");
				miningUrns.setBounds(250, 76, 148, 24);
				jp1.add(miningUrns);

				final JComboBox superheatBar = new JComboBox();
				superheatBar.setBounds(250, 128, 133, 28);
				superheatBar.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				superheatBar.setModel(new DefaultComboBoxModel(new Bars[] {
						Bars.BRONZE, Bars.IRON, Bars.STEEL, Bars.SILVER, Bars.GOLD, Bars.MITHRIL, Bars.ADAMANT, Bars.RUNITE
				}));
				jp1.add(superheatBar);
				
				
				final JCheckBox smithingUrns = new JCheckBox("Use Smithing Urns");
				smithingUrns.setBounds(250, 43, 133, 24);
				jp1.add(smithingUrns);

				jtp.addTab("Rune Rocks", jp3);
				jp3.setLayout(null);

				final JCheckBox chckbxUseMemberWorlds = new JCheckBox(
						"User member worlds");
				chckbxUseMemberWorlds.setBounds(12, 12, 179, 24);
				jp3.add(chckbxUseMemberWorlds);

				final JCheckBox chckbxUseHighriskWorlds = new JCheckBox(
						"Use high-risk worlds");
				chckbxUseHighriskWorlds.setBounds(12, 63, 179, 24);
				chckbxUseHighriskWorlds.setEnabled(false);
				jp3.add(chckbxUseHighriskWorlds);

				final JCheckBox chckbxUseSkillWorlds = new JCheckBox(
						"Use skill worlds");
				chckbxUseSkillWorlds.setBounds(12, 39, 155, 24);
				chckbxUseSkillWorlds.setEnabled(false);
				jp3.add(chckbxUseSkillWorlds);

				final JCheckBox chckbxRunBackOn = new JCheckBox("Run back on death");
				chckbxRunBackOn.setBounds(12, 111, 179, 24);
				jp3.add(chckbxRunBackOn);

				JLabel lblFood = new JLabel("Food:");
				lblFood.setBounds(216, 15, 70, 18);
				jp3.add(lblFood);

				JLabel lblFoodAmt = new JLabel("Amount:");
				lblFoodAmt.setBounds(215, 75, 70, 28);
				jp3.add(lblFoodAmt);

				final JSlider foodAmount = new JSlider();
				foodAmount.setBounds(215, 105, 123, 48);
				foodAmount.setMajorTickSpacing(1);
				foodAmount.setMaximum(28);
				foodAmount.setMinimum(0);
				foodAmount.setValue(0);
				jp3.add(foodAmount);

				final JComboBox foodSelection = new JComboBox();
				foodSelection.setBounds(213, 37, 123, 28);
				foodSelection.setModel(new DefaultComboBoxModel(new Food[] {
						Food.LOBSTER, Food.TROUT, Food.SALMON, Food.PIKE,
						Food.MONKFISH, Food.SHARK, Food.SWORDFISH }));
				foodSelection.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				jp3.add(foodSelection);

				JButton startAdventure = new JButton();
				startAdventure.setBounds(149, 12, 94, 18);
				jp4.add(startAdventure);

				jtp.addTab("Adventure Mode", jp4);
				class AdventureAction extends AbstractAction {

					public AdventureAction() {
						putValue(NAME, "Start Adventure!");
						putValue(SHORT_DESCRIPTION,
								"Click to start the adventure, no other settings needed!");
					}

					@Override
					public void actionPerformed(ActionEvent arg0) {

						RandomEventGift reg = new RandomEventGift();
						Strategy regs = new Strategy(reg, reg);
						provide(regs);
						
						MoveBox mb = new MoveBox();
						Strategy mbs = new Strategy(mb, mb);
						provide(mbs);
						
						FailSafe fs = new FailSafe();
						Strategy fss = new Strategy(fs, fs);
						provide(fss);
						
						AdventureSettingsManager asm = new AdventureSettingsManager();
						Strategy asms = new Strategy(asm, asm);
						provide(asms);

						AdventurePathing ap = new AdventurePathing();
						Strategy aps = new Strategy(ap, ap);
						provide(aps);

						Mining m = new Mining();
						Strategy ms = new Strategy(m, m);
						provide(ms);

						Banking b = new Banking();
						Strategy bs = new Strategy(b, b);
						provide(bs);

						WalkToBank wtb = new WalkToBank();
						Strategy wtbs = new Strategy(wtb, wtb);
						provide(wtbs);

						WalkToMine wtm = new WalkToMine();
						Strategy wtms = new Strategy(wtm, wtm);
						provide(wtms);

						LRCDepositingManager lrcd = new LRCDepositingManager();
						Strategy lrcds = new Strategy(lrcd, lrcd);
						provide(lrcds);

						DeathWalker dw = new DeathWalker();
						Strategy dws = new Strategy(dw, dw);
						provide(dws);

						WalkToLRCBank wtbz = new WalkToLRCBank();
						Strategy wtbzs = new Strategy(wtbz, wtbz);
						provide(wtbzs);

						ScriptSettings.mode = Modes.ADVENTURE;
						ScriptSettings.rock = Rocks.CLAY;
						ScriptSettings.pickaxe = Pickaxes.BRONZE_PICK;
						ScriptSettings.location = Locations.VARROCK_WEST;
						ScriptSettings.miningArea = vWestArea;
						ScriptSettings.bankArea = vWestBankArea;

						setOurRockTo(Rocks.CLAY);
						setOurRock2To(Rocks.TIN);

						selectedRocks = new Filter<SceneObject>() {
							@Override
							public boolean accept(SceneObject arg0) {
								if (ScriptSettings.rocks == null) {
									log.info("ScriptSetting f null");
								}
								for (int rockID : ScriptSettings.rocks) {
									if (arg0.getId() == rockID
											|| arg0.getId() == 2491) {
										return (ScriptSettings.miningArea.contains(arg0.getLocation()));
									}
								}
								return false;

							}
						};

						log.info("Primary rocks set");
						secondaryRocks = new Filter<SceneObject>() {

							@Override
							public boolean accept(SceneObject arg0) {
								if (ScriptSettings.secondaryRocks == null) {
									log.info("Null secondary");
								}

								for (int rokid : ScriptSettings.secondaryRocks) {
									if (arg0.getId() == rokid) {
										return ScriptSettings.miningArea.contains(arg0.getLocation());
									}
								}
								return false;
							}
						};

						log.info("Adventure mode set!");
						guiDone = true;
						
						dispose();
					}
				}

				startAdventure.setAction(new AdventureAction());

				class SwingAction extends AbstractAction {

					public SwingAction() {
						putValue(NAME, "Start!");
						putValue(SHORT_DESCRIPTION,
								"Click to start the script once you are ready");
					}

					public void actionPerformed(ActionEvent e) {
						ScriptSettings.location = (Locations) location
								.getSelectedItem();
						ScriptSettings.pickaxe = (Pickaxes) pickaxe
								.getSelectedItem();
						ScriptSettings.mode = (Modes) mode.getSelectedItem();
						ScriptSettings.rock = (Rocks) rock.getSelectedItem();
						ScriptSettings.rock2 = (Rocks) rock2.getSelectedItem();

						ScriptSettings.useDeathWalker = chckbxRunBackOn
								.isSelected();
						ScriptSettings.antiBanOn = chckbxAntiban.isSelected();
						// chckbxRockPrediction.isSelected();
						// ScriptSettings.skillWorlds =
						// chckbxUseSkillWorlds.isSelected();
						// ScriptSettings.highRisk =
						// chckbxUseHighriskWorlds.isSelected();
						ScriptSettings.members = chckbxUseMemberWorlds.isSelected();
						ScriptSettings.free = true;
						ScriptSettings.rockPrice = 200;

						ScriptSettings.useMiningUrns = miningUrns.isSelected();
						ScriptSettings.useSmithingUrns = smithingUrns.isSelected();
						ScriptSettings.useLavaTitan = lavaTitan.isSelected();
						ScriptSettings.useSuperheat = superHeat.isSelected();
						
						if (ScriptSettings.useSuperheat) {
							ScriptSettings.barToSuperheat = (Bars) superheatBar.getSelectedItem();
							setOurBarTo(ScriptSettings.barToSuperheat);
						}
						
						ScriptSettings.foodWithdrawAmount = foodAmount.getValue();
						ScriptSettings.food = (Food) foodSelection
								.getSelectedItem();

						log.info("Set location to " + ScriptSettings.location);
						log.info("Set pickaxe to " + ScriptSettings.pickaxe);
						log.info("Set mode to " + ScriptSettings.mode);
						log.info("Set rock to " + ScriptSettings.rock);
						log.info("Successfully loaded user selections.");
						
						setFoodTo(ScriptSettings.food);
						setOurPickaxeTo(ScriptSettings.pickaxe);
						
						if (!ScriptSettings.useSuperheat) {
							setOurRockTo(ScriptSettings.rock);
							setOurRock2To(ScriptSettings.rock2);
						}
						setOurLocationTo(ScriptSettings.location);
						
						provideAntiBan(ScriptSettings.antiBanOn);
						log.info("AntiBan check..");
						provideStrategies(ScriptSettings.location,
								ScriptSettings.mode);

						selectedRocks = new Filter<SceneObject>() {
							@Override
							public boolean accept(SceneObject arg0) {
								if (ScriptSettings.rocks == null) {
									log.info("ScriptSetting f null");
								}
								for (int rockID : ScriptSettings.rocks) {
									if (arg0.getId() == rockID
											|| arg0.getId() == 2491) {
										return ScriptSettings.miningArea.contains(arg0.getLocation());
									}
								}
								return false;

							}
						};
						log.info("Primary rocks set");
						secondaryRocks = new Filter<SceneObject>() {

							@Override
							public boolean accept(SceneObject arg0) {
								if (ScriptSettings.secondaryRocks == null) {
									log.info("Null secondary");
								}

								for (int rokid : ScriptSettings.secondaryRocks) {
									if (arg0.getId() == rokid) {
										return ScriptSettings.miningArea.contains(arg0.getLocation());
									}
								}
								return false;
							}
						};
						log.info("Secondary rocks set");
						log.info("GuiDone: true");
						guiDone = true;
						dispose();
					}
				}

				Action startScript = new SwingAction();

				JButton btnNewButton = new JButton("New button");
				btnNewButton.setAction(startScript);
				btnNewButton.setBounds(329, 187, 96, 30);
				jp1.add(btnNewButton);
			}
		}
		
		
		public boolean nrObjs(final Filter<SceneObject> objs, final Tile loc) {
			SceneObject obj = SceneEntities.getNearest(objs);
			if (obj != null) {
				return Calculations.distance(obj.getLocation(), loc) <= 5;
			}
			return false;
		}
		
		public boolean nrObj(final int id, final Tile loc) {
			SceneObject obj = SceneEntities.getNearest(id);
			if (obj != null) {
				return Calculations.distance(obj.getLocation(), loc) <=5;
			}
			return false;
		}
		
		public void walkToLadderFromHeroes() {
			Walking.newTilePath(innerGuildToLadder).traverse();
		}
		
		public boolean havePrimaryRequirement() {
			int primaryCheck = Inventory.getCount(ScriptSettings.primaryOre);
			return primaryCheck >= ScriptSettings.primaryRequirement;
		}
		
		public boolean haveSecondaryRequirement() {
			int secondaryCheck = Inventory.getCount(ScriptSettings.secondaryOre);
			return secondaryCheck >= ScriptSettings.secondaryRequirement;
		}
		
		public boolean inMiningAnimation() {
			for (int i = 0; i < ScriptSettings.animationIDs.length;i++) {
				if (Players.getLocal().getAnimation() == ScriptSettings.animationIDs[i]) {
					return true;
				}
			}
			return false;
		}
		
		public void turnRunOn() {
			if (Walking.getEnergy() >= 70 && !Walking.isRunEnabled()) {
				Walking.setRun(true);
			}
		}
		
		public boolean detectedPickaxe() {
			Item[] inv = Inventory.getItems();
			int id = ScriptSettings.bankItemsToIgnore[0];
			
			for (int i = 0; i < inv.length; i++) {
				if (inv[i].getId() == id) {
					return true;
				}
			}
			
			int[] itms = Players.getLocal().getAppearance();
			for (int i = 0; i < itms.length; i++) {
				if (itms[i] == id) {
					return true;
				}
			}
			return false;
		}
		
		public void setOurBarTo(Bars bar) {
			
			switch (bar) {
			case STEEL:
				ScriptSettings.primaryOre = iron;
				setOurRockTo(Rocks.IRON);
				setOurRock2To(Rocks.COAL);
				break;
			case IRON:
				ScriptSettings.primaryOre = iron;
				setOurRockTo(Rocks.IRON);
				setOurRock2To(Rocks.IRON);
				break;
			case SILVER:
				ScriptSettings.primaryOre = silver;
				setOurRockTo(Rocks.SILVER);
				setOurRock2To(Rocks.SILVER);
				break;
			case GOLD:
				ScriptSettings.primaryOre = gold;
				setOurRockTo(Rocks.GOLD);
				setOurRock2To(Rocks.GOLD);
				break;
			case MITHRIL:
				ScriptSettings.primaryOre = mith;
				setOurRockTo(Rocks.MITH);
				setOurRock2To(Rocks.COAL);
				break;
			case ADAMANT:
				ScriptSettings.primaryOre = addy;
				setOurRockTo(Rocks.ADDY);
				setOurRock2To(Rocks.COAL);
				break;
			case RUNITE:
				ScriptSettings.primaryOre = rune;
				setOurRockTo(Rocks.RUNE);
				setOurRock2To(Rocks.COAL);
				break;
			}
			
			
		}
		public boolean canCastSuperheat() {

			switch (ScriptSettings.barToSuperheat) {
			case BRONZE:
				return hasBronzeIngredients();
			case IRON:
				return hasIronIngredients();
			case STEEL:
				return hasSteelIngredients();
			case MITHRIL:
				return hasMithrilIngredients();
			case SILVER:
				return hasSilverIngredients();
			case GOLD:
				return hasGoldIngredients();
			case ADAMANT:
				return hasAdamantIngredients();
			case RUNITE:
				return hasRuniteIngredients();
			}
			
			return false;
		}
		
		public boolean hasSteelIngredients() {
			return Inventory.getCount(iron) >= 1 && Inventory.getCount(coal) >= 2;
		}
		
		public boolean hasIronIngredients() {
			return Inventory.getCount(iron) >= 1;
		}
		
		public boolean hasBronzeIngredients() {
			return Inventory.getCount(copper) >= 1 && Inventory.getCount(tin) >= 1;
		}
		
		public boolean hasAdamantIngredients() {
			return Inventory.getCount(coal) >= 6 && Inventory.getCount(addy) >= 1;
		}
		
		public boolean hasRuniteIngredients() {
			return Inventory.getCount(coal) >= 8 && Inventory.getCount(rune) >= 1;
		}
		
		public boolean hasMithrilIngredients() {
			return Inventory.getCount(mith) >= 1 && Inventory.getCount(coal) >= 4;
		}
		
		public boolean hasSilverIngredients() {
			return Inventory.getCount(silver) >= 1;
		}
		
		public boolean hasGoldIngredients() {
			return Inventory.getCount(gold) >= 1;
		}
		
		public boolean wearingEnchantedTiara() {
			int[] it = Players.getLocal().getAppearance();
			for (int i = 0; i < it.length;i++) {
				if (it[i] == 11969) {
					return true;
				}
			}
			return false;
		}
		
		public void walkToInnerHeroesGuild() {
			Walking.newTilePath(heroesLadderToInnerMine).traverse();
		}
		
		public void walkToCraftGuild() {
			Walking.newTilePath(ScriptSettings.toMinePath).traverse();
		}
		
		public void walkToPortSarimDepo() {
			ScriptSettings.reverseToMinePath.traverse();
		}
		
		public void depositAllAtRimmington() {
			if (!DepositBox.isOpen()) {
				DepositBox.open();
			}
			depositBoxDepositAll();
		}
		
		public void depositBoxDepositAll() {
			Item[] it = DepositBox.getItems();
			for (int i = 0; i < it.length;i++) {
				if (ignoreableDepositItems.accept(it[i])) {
					DepositBox.deposit(it[i].getId(), 29);
				}
			}
			DepositBox.close();
		}
		
		public boolean nearShantayPass() {
			SceneObject obj = SceneEntities.getNearest(3550);
			if (obj != null) {
				return Calculations.distance(Players.getLocal().getLocation(), obj.getLocation()) <= 15;
			}
			return false;
		}
		
		public void travelToShantayPass() {
			intrNPC(2292, "Travel");
			Widgets.get(1188).getChild(2).interact("Continue");
			Widgets.get(1184).getChild(19).interact("Continue");
		}
		
		public void walkToDesertCamp() {
			ScriptSettings.reverseToMinePath.traverse();
		}
		
		public void travelToDesertCamp() {
			intrNPC(2291, "Travel");
			Widgets.get(1188).getChild(23).interact("Continue");
			Widgets.get(1184).getChild(19).interact("Continue");
		}
		
		boolean clickedTrade = false;
		public void quarryWithdrawWaterskins() {
			if (!Bank.isOpen()) {
				Bank.open();
			}
			if (Bank.getItemCount(true, waterskins) < 10) {
				if (Bank.isOpen()) {
					Bank.close();
				}
				
				if (!clickedTrade) {
					intrNPC(836, "Trade");
					clickedTrade = true;
				}
				
				if (clickedTrade) {
					Widgets.get(1265).getChild(26).getChild(0).interact("Buy 10");
				}
			} else {
				if (Inventory.getCount(waterskins) < 10) {
					Bank.search("waterskin");
					for (int i = 0; i < waterskinIDs.length; i++) {
						if (Inventory.getCount() < 10) {
							Bank.withdraw(waterskinIDs[i], 10);
						} else {
							Bank.close();
						}
					}
				}
			}
		}
		
		public void goThroughShantayPass() {
			intrObj(3550, "Go-through");
		}
		
		
		public void intrObj(int id, String option) {
			SceneObject obj = SceneEntities.getNearest(id);
			if (obj != null) {
				if (!obj.isOnScreen()) {
					Walking.walk(obj.getLocation());
					Camera.turnTo(obj);
				}
				obj.interact(option);
			}
		}

	public boolean nrNPC(final int id, final Tile loc) {
		NPC npc = NPCs.getNearest(id);
		if (npc != null) {
			return Calculations.distance(npc.getLocation(), loc) <= 5;
		}
		return false;
	}
		
	public void intrNPC(final int id, final String option) {
		NPC npc = NPCs.getNearest(id);
		if (npc != null) {
			if (!npc.isOnScreen()) {
				Walking.walk(npc.getLocation());
				Camera.turnTo(npc);
			}
			npc.interact(option);
		}
	}

	public void buyShantayPass() {
		intrNPC(836, "Buy-pass");
	}

	public boolean haveShantayPass() {
		return Inventory.getCount(1854) > 0;
	}

	public boolean nearShantayTravel() {
		NPC travel = NPCs.getNearest(2291);
		if (travel != null) {
			return Calculations.distance(travel, Players.getLocal()
					.getLocation()) <= 7;
		}
		return false;
	}

		public void buyPickaxes() {
			if (Inventory.getCount(1273) < 1) {
				GrandExchange.buyItem("Mithril pickaxe", 1273, false);
			} else if (Inventory.getCount(1275) < 1) {
				GrandExchange.buyItem("Rune pickaxe", 1275, false);
			} else if (Inventory.getCount(1271) < 1) {
				GrandExchange.buyItem("Adamant pickaxe", 1271, false);
			}
		}
		
		public boolean inAdventurePart7() {
			return coinsInPouchGreaterThan(100) && GEArea.contains(Players.getLocal().getLocation()) && Inventory.getCount(1273)+Inventory.getCount(1275)+Inventory.getCount(1271) != 3;
		}
		
		public void sellClay() {
			if (Inventory.getCount(435) < 0) {
				withdrawNotedClay();
			} else if (!coinsInPouchGreaterThan(100)) {
				GrandExchange.sellItem(435);
			}
		}	
		
		private static class GrandExchange {
			
			
			private static boolean priceDecreased = false;
			private static boolean confirmedOffer = false;
			private static boolean madeOffer = false;
			private static boolean collectedCoins = false;
			private static boolean madeSellOffer = false;
			private static boolean madeBuyOffer = false;
			private static boolean chooseItemScreen = false;
			private static boolean searchedForName = false;
			private static boolean clickedName = false;
			private static boolean increasedPrice = false;
			private static boolean confirmedBuyOffer = false;
			private static boolean collectedItems = false;
			private static boolean viewedOffer = false;
			private static boolean increasedQuantity = false;
		
			
			
			private static Filter<NPC> GEClerks = new Filter<NPC>() {

				@Override
				public boolean accept(NPC arg0) {
					return (arg0.getName().equals("Grand Exchange clerk"));
				}

			};
			
			public static boolean searchBarOnScreen() {
				return Widgets.get(389).getChild(1).isOnScreen();
			}
			
			public static void buyItem(String name, int id, boolean noted) {
				Widget wa = new Widget(105);
				WidgetChild wx = Widgets.get(449).getChild(1);
				WidgetChild wo = wa.getChild(139);
				WidgetChild wi = wa.getChild(139);

				if (!exchangeOpen()) {
					ScriptSettings.status = "Opening GE";
					NPC ge = NPCs.getNearest(GEClerks);
					if (ge != null) {
						Camera.turnTo(ge.getLocation());
						ge.interact("Exchange");
					}
				}

				if (!madeBuyOffer) {
					for (int i = 0; i < buyBoxes.length; i++) {

						WidgetChild wz = wa.getChild(buyBoxes[i]);

						if (wz.isOnScreen()) {
							ScriptSettings.status = "Making buy offer.";
							if (wz.interact("Make Buy Offer")) {
								madeBuyOffer = true;
							}
							break;
						}

					}

				}

				if (!wx.isOnScreen() && madeBuyOffer && !chooseItemScreen) {
					ScriptSettings.status = "Choosing item.";
					if (wo.interact("Choose Item")) {
						chooseItemScreen = true;
					}

					if (chooseItemScreen && searchBarOnScreen() && !searchedForName) {
						ScriptSettings.status = "Searching for " + name;
						Keyboard.sendText("     "+name, true);
						searchedForName = true;
					}

					if (searchedForName && wi.getChildId() != id && !clickedName) {
						ScriptSettings.status = "Grabbing..";
						WidgetChild wocs = Widgets.get(389).getChild(4);
						if (wocs != null) {
							for (int i = 0; i < 100; i++) {
								System.out.println("NAMEVAR:" + name);
								System.out.println("TEXTVAR: "
										+ wocs.getChild(i).getText());
								if (wocs.getChild(i).getText()
										.equalsIgnoreCase(name)) {
									wocs.getChild(i).click(true);
									clickedName = true;
									break;
								}
							}
						}
					}

					if (clickedName && !increasedQuantity) {
						ScriptSettings.status = "Increasing quantitiy.";
						if (Widgets.get(105).getChild(160).interact("Add 1")) {
							increasedQuantity = true;
						}
					}

					if (increasedQuantity && !increasedPrice
							&& Widgets.get(105).getChild(139).getChildId() == id) {
						ScriptSettings.status = "Increasing price.";
						for (int i = 0; i < 3; i++) {
							Widgets.get(105).getChild(179)
									.interact("Increase Price");
						}
						increasedPrice = true;
					}

					if (!confirmedBuyOffer && increasedPrice) {
						ScriptSettings.status = "Confirming Offer.";
						if (Widgets.get(105).getChild(187)
								.interact("Confirm Offer")) {
							confirmedBuyOffer = true;
						}
					}

					if (confirmedBuyOffer && !viewedOffer) {
						ScriptSettings.status = "Viewing offer.";
						if (Widgets.get(105).getChild(19).getChild(12)
								.interact("View Offer")) {
							viewedOffer = true;
						}
					}

					if (viewedOffer && !collectedItems) {
						Widget z = Widgets.get(105);
						WidgetChild ab = z.getChild(208);
						WidgetChild ac = z.getChild(206);

						ScriptSettings.status = "Collecting items.";
						boolean collectedItem = false;
						if (ab.getChildId() == id) {
							if (!noted) {
								if (ab.click(true)) {
									collectedItem = true;
								}
							} else {
								ab.click(false);
								String[] opts = Menu.getOptions();
								for (int i = 0; i < opts.length; i++) {
									if (opts[i].contains("noted")) {
										Menu.select(opts[i]);
										collectedItem = true;
									}
								}
							}
						} else {
							if (ab.click(true)) {
							}
						}

						if (ac.getChildId() == id) {
							if (!noted) {
								if (ac.click(true)) {
									collectedItem = true;
								}
							} else {
								ac.click(false);
								String[] opts = Menu.getOptions();
								for (int i = 0; i < opts.length; i++) {
									if (opts[i].contains("noted")) {
										Menu.select(opts[i]);
										collectedItem = true;
									}
								}
							}
						} else {
							if (ac.click(true)) {
							}
						}

						if (collectedItem) {
							collectedItems = true;
						}
					}
				}
			}

			public static void sellItem(int id) {
				Widget w = new Widget(107);
				Widget wa = new Widget(105);

				if (!exchangeOpen()) {
					ScriptSettings.status = "Opening GE";
					NPC ge = NPCs.getNearest(GEClerks);
					if (ge != null) {
						Camera.turnTo(ge.getLocation());
						ge.interact("Exchange");
					}
				}

				if (!madeSellOffer) {
					for (int i = 0; i < sellBoxes.length; i++) {
						if (wa.getChild(sellBoxes[i]).isOnScreen()) {
							wa.getChild(sellBoxes[i]).interact("Make Sell Offer");
							madeSellOffer = true;
							break;
						}
					}
				}
				
				if (madeSellOffer && !madeOffer) {
					ScriptSettings.status = "Making offer.";
					w.getChild(18).getChild(getGEInvIndex(id)).interact("Offer");
					madeOffer = true;
				}

				if (madeOffer && !priceDecreased) {
					ScriptSettings.status = "Decreasing price..";
					wa.getChild(181).interact("Decrease Price");
					priceDecreased = true;
				}

				if (!confirmedOffer && priceDecreased) {
					ScriptSettings.status = "Confirming offer.";
					wa.getChild(187).interact("Confirm Offer");
					confirmedOffer = true;
				}

				if (confirmedOffer && !collectedCoins) {
					ScriptSettings.status = "Collecting coins.";
					wa.getChild(19).getChild(12).interact("View Offer");
					if (wa.getChild(206).interact("Collect Coins")) {
						collectedCoins = true;
					}
					wa.getChild(206).click(true);
				}
			}

			public static int getGEInvIndex(int id) {
				WidgetChild[] wcs = Widgets.get(107).getChildren();

				for (int i = 0; i < wcs.length; i++) {
					if (wcs[i].getId() == id) {
						return i;
					}
				}
				return 0;
			}

			public static boolean exchangeOpen() {
				Widget w = Widgets.get(105);
				if (w != null) {
					if (w.getChild(1).isOnScreen()) {
						return true;
					}
				}
				return false;
			}
		}
		
		
		
		public void withdrawNotedClay() {
			if (Inventory.getCount(435) < 1) {
				if (!Bank.isOpen()) {
					Bank.open();
				}
				if (!Bank.isWithdrawNotedEnabled()) {
					Bank.setWithdrawNoted(true);
				}
				Bank.search("Clay");
				Bank.withdraw(434, 99999);
				Bank.close();
			}
		}
		
		public boolean soldClay() {
			return coinsInPouchGreaterThan(100);
		}
		
		public boolean inAdventurePart6() {
			return GEArea.contains(Players.getLocal().getLocation()) && !soldClay();
		}
		
		public boolean inAdventurePart5() {
			return ScriptSettings.mode == Modes.ADVENTURE && !GEArea.contains(Players.getLocal().getLocation()) && Skills.getLevel(Skills.MINING) >= 25;
		}
		
		public void walkToGE() {
			Walking.newTilePath(evBankToGE).traverse();
		}
		
		public boolean inAdventurePart4() {
			return Skills.getLevel(Skills.MINING) >= 25 && ScriptSettings.bankArea.contains(Players.getLocal().getLocation()) && Inventory.getCount() == 0;
		}

		public boolean coinsInPouchGreaterThan(int n) {
			WidgetChild w = new WidgetChild(new Widget(548), 196);
			if (w != null) {
				String pouch = w.getText();
				pouch = pouch.substring(0, pouch.length() - 1);
				if (pouch != null && pouch.length() >= 1) {
					Integer i = Integer.parseInt(pouch);
					if (i >= n) {
						return true;
					}
				}
			}
			return false;
		}
		
		public boolean coinsInPouch() {
			WidgetChild w = new WidgetChild(new Widget(548), 196);
			if (w != null) {
				String pouch = w.getText();
				pouch = pouch.substring(0, pouch.length() - 1);
				if (pouch != null && pouch.length() >= 1) {
					log.info("Coins in pouch: " + pouch);
					Integer i = Integer.parseInt(pouch);
					if (i >= 1) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean inAdventurePart3() {
			boolean b = ScriptSettings.miningArea.contains(Players.getLocal()
					.getLocation());
			return b || (ScriptSettings.bankArea.contains(Players.getLocal().getLocation()) && ScriptSettings.mode == Modes.ADVENTURE);
		}

		private Image getImage(String url) {
			try {
				return ImageIO.read(new URL(url));
			} catch (IOException e) {
				return null;
			}
		}

		public void walkToVarrockWest() {
			Walking.newTilePath(bobsAxesToVarrockWest).traverse();
		}

		public boolean inAdventurePart2() {
			boolean miningLessThan25 = Skills.getLevel(Skills.MINING) < 25 && !ScriptSettings.miningArea.contains(Players.getLocal().getLocation()) && ScriptSettings.mode == Modes.ADVENTURE;
			return miningLessThan25;
		}

		public boolean inAdventurePart1() {
			return (lumbridgeArea.contains(Players.getLocal().getLocation())
					&& Skills.getLevel(Skills.MINING) <= 25 && Inventory
						.getCount(1265) != 1);
		}

		public boolean nearDepositBox() {
			SceneObject dep = SceneEntities.getNearest(depoBoxes);
			if (dep != null) {
				return Calculations.distance(Players.getLocal().getLocation(),
						dep.getLocation()) <= 2;
			}
			return false;
		}

		public void DMineDepositAllExceptIgnoreables() {
			if (!DepositBox.isOpen()) {
				DepositBox.open();
			}

			if (DepositBox.isOpen()) {
				DepositBox.deposit(ScriptSettings.rockID, 29);
				log.info("Depositing..");
				for (int i = 0; i < gemIDs.length; i++) {
					DepositBox.deposit(gemIDs[i], 29);
					log.info("Deposited gems.");

				}
			}

			if (DepositBox.getItemCount(false, ignoreableDepositItems) == 0) {
				DepositBox.close();
			}
		}

		public void walkToDownStairs() {
			Walking.newTilePath(ScriptSettings.toMinePath).traverse();
		}

		
		public void intrObjs(final Filter<SceneObject> objs, String action) {
			SceneObject door = SceneEntities.getNearest(objs);
			if (door != null) {
				Tile loc = door.getLocation();
				if (!door.isOnScreen()) {
					Walking.walk(loc);
					Camera.turnTo(loc);
				}
				door.interact(action);
			}
		}

		boolean diedRecently = false;

		public boolean haveDiedRecently() {
			if (Players.getLocal().getAnimation() == 836) {
				diedRecently = true;
			}
			return false;
		}

		public boolean inInventory(int id) {
			Item[] inv = Inventory.getItems();

			for (int i = 0; i < inv.length; i++) {
				if (inv[i].getId() == id) {
					return true;
				}
			}
			
			return false;
		}

		public boolean LRCBankOpen() {
			Widget w = Widgets.get(11);
			if (w != null) {
				return w.validate();
			}
			return false;
		}

		public void LRCdepositAllExceptIgnoreables() {
			SceneObject depo = SceneEntities.getNearest(45079);
			if (depo != null) {
				depo.interact("Deposit");
			}

			if (LRCBankOpen()) {
				WidgetChild wc = Widgets.get(11, 17);
				if (wc != null) {
					WidgetChild[] wcs = wc.getChildren();
					if (wcs != null) {
						for (int i = 0; i < wcs.length; i++) {
							if (wcs[i] != null) {
								if (idsToIgnore.accept(wcs[i].getChildId())) {
									Mouse.hop(wcs[i].getAbsoluteX(),
											wcs[i].getAbsoluteY());
									Mouse.click(wcs[i].getCentralPoint(), true);
									Point p = Widgets.get(11, 15).getCentralPoint();
									Mouse.hop((int) p.getX(), (int) p.getY());
									Mouse.click(p, true);
								}
							}
						}
					}
				}
			}
		}

		public boolean beingAttackedByMelee() {
			if (Players.getLocal().isInCombat()) {
				Character n = Players.getLocal().getInteracting();
				if (n.getId() == 8832) {
					return true;
				}
			}
			return false;
		}

		public boolean beingAttackedByRanged() {
			if (Players.getLocal().isInCombat()) {
				Character n = Players.getLocal().getInteracting();
				if (n.getId() == 8833) {
					return true;
				}
			}
			return false;
		}

		public void teleportUrns() {
			Item[] ik = Inventory.getItems(completeUrn);
			if (ik != null) {
				for (int i = 0; i < ik.length; i++) {
					Item it = ik[i];
					if (it != null) {
						WidgetChild w = it.getWidgetChild();
						if (w != null) {
							w.interact("Teleport");
						}
					}
				}
			}
		}

		public boolean hasCompleteUrns() {
			return Inventory.getCount(completeUrn) > 0;
		}

		public void dropVial() {
			Item i = Inventory.getItem(229);
			if (i != null) {
				WidgetChild w = i.getWidgetChild();
				if (w != null) {
					Mouse.hop(w.getAbsoluteX(), w.getAbsoluteY());
					Mouse.click(false);
					if (Menu.contains("Drop")) {
						Mouse.hop(w.getAbsoluteX(), w.getAbsoluteY() - 20);
						Mouse.click(true);
					}
				}
			}
		}

		public void drinkSummoningPotion() {
			Item[] p = Inventory.getItems(summoningPotions);
			if (p != null) {
				for (int i = 0; i < p.length; i++) {
					WidgetChild w = p[i].getWidgetChild();
					if (w != null) {
						w.interact("Drink");
						dropVial();
					}
				}
			}
		}

		public void summonLavaTitan() {
			Item i = Inventory.getItem(12788);
			if (i != null) {
				WidgetChild w = i.getWidgetChild();
				if (w != null) {
					w.interact("Summon");
					ScriptSettings.lavaTitanSummoned = true;
				}
			}
		}

		public boolean hasLavaTitanPouch() {
			return Inventory.getCount(12788) > 0;
		}

		public void castSuperHeat() {

			if (Tabs.getCurrent() != Tabs.MAGIC) {
				Tabs.MAGIC.open();
				log.info("Opening magic tab.");
			}

			Widget magic = new Widget(192);
			if (magic != null) {
				WidgetChild superheat = magic.getChild(50);
				if (superheat != null) {
					Point lastMouseLoc = new Point(Mouse.getX(), Mouse.getY());
					Point hopP = new Point(superheat.getCentralPoint());
					Mouse.hop((int) hopP.getX(), (int) hopP.getY());
					superheat.interact("Cast");
					
					Item ore = Inventory.getItem(ScriptSettings.primaryOre);
					log.info("Casting superheat on : " + ore.getName());
					if (ore != null) {
						WidgetChild menu = ore.getWidgetChild();
						if (menu != null) {
							Mouse.hop(menu.getAbsoluteX(), menu.getAbsoluteY());
							Mouse.click(true);
							Mouse.hop((int) lastMouseLoc.getX(),
									(int) lastMouseLoc.getY());
						}
					}
				}
			}
		}

		public boolean hasSuperHeatItems() {
			return Inventory.getItem(561) != null;
		}

		public boolean aroundAnyRock() {
			return nearLRCCoalRock() || nearLRCGoldRock();
		}

		public boolean nearLRCCoalRock() {
			SceneObject nearest1 = SceneEntities.getNearest(LRCCoalRock);
			if (nearest1 != null) {
				return (Calculations.distance(nearest1.getLocation(), Players
						.getLocal().getLocation()) <= 5);
			}
			return false;
		}

		public boolean nearLRCGoldRock() {
			SceneObject nearest1 = SceneEntities.getNearest(LRCGoldRock);
			if (nearest1 != null) {
				return (Calculations.distance(nearest1.getLocation(), Players
						.getLocal().getLocation()) <= 5);
			}
			return false;
		}

		public boolean inLRCBankArea() {
			return SceneEntities.getNearest(45079) != null;
		}

		public void withdrawAllWithdrawables() {

			for (int i = 0; i < ScriptSettings.bankItemsToWithdraw.length; i++) {
				String itm = ScriptSettings.bankItemsToWithdraw[i].toLowerCase();
				if (!inInventory(ScriptSettings.bankDatabase.get(itm))) {
					Bank.search(itm);
					Bank.withdraw(ScriptSettings.bankDatabase.get(itm),
							withdrawAmount(itm));
				}
			}
		}

		public int withdrawAmount(String itemName) {

			if (itemName.contains("Pickaxe")) {
				return 1;
			} else if (itemName.equalsIgnoreCase("trout")
					|| itemName.equalsIgnoreCase("salmon")
					|| itemName.equalsIgnoreCase("lobster")
					|| itemName.equalsIgnoreCase("swordfish")
					|| itemName.equalsIgnoreCase("shark")
					|| itemName.equalsIgnoreCase("monkfish")
					|| itemName.equalsIgnoreCase("pike")) {
				return ScriptSettings.foodWithdrawAmount;
			} else
				return 1;
		}

		public void depositAllExceptIgnoreables() {
			Item[] count = Inventory.getItems(ignoreableDepositItems);
			for (int i = 0; i < count.length; i++) {
				Bank.deposit(count[i].getId(), 28);
			}
		}

		public void provideAntiBan(boolean b) {
			if (b) {
				AntiBan antiBan = new AntiBan();
				Strategy antiBans = new Strategy(antiBan, antiBan);
				provide(antiBans);
			}
		}


		public void setFoodTo(Food food) {
			if (ScriptSettings.location == Locations.RUNE_ROCKS) {
				switch (food) {
				case TROUT:
					ScriptSettings.foodID = 333;
					ScriptSettings.bankItemsToWithdraw[1] = "Trout";

					break;
				case LOBSTER:
					ScriptSettings.foodID = 379;
					ScriptSettings.bankItemsToWithdraw[1] = "Lobster";

					break;
				case SWORDFISH:
					ScriptSettings.foodID = 373;
					ScriptSettings.bankItemsToWithdraw[1] = "Swordfish";

					break;
				case PIKE:
					ScriptSettings.foodID = 351;
					ScriptSettings.bankItemsToWithdraw[1] = "Pike";

					break;
				case SHARK:
					ScriptSettings.foodID = 385;
					ScriptSettings.bankItemsToWithdraw[1] = "Shark";

					break;
				case MONKFISH:
					ScriptSettings.foodID = 7946;
					ScriptSettings.bankItemsToWithdraw[1] = "Monkfish";

					break;
				case SALMON:
					ScriptSettings.foodID = 329;
					ScriptSettings.bankItemsToWithdraw[1] = "Salmon";

					break;
				}
			}
		}

		public void provideStrategies(Locations loc, Modes mode) {

			ComputeOnce co = new ComputeOnce();
			Strategy cos = new Strategy(co, co);
			provide(cos);
			
			SafeLRC sl = new SafeLRC();
			Strategy sls = new Strategy(sl, sl);
			provide(sls);
			
			if (ScriptSettings.useMiningUrns || ScriptSettings.useSmithingUrns) {
				
				UrnManager um = new UrnManager();
				Strategy ums = new Strategy(um, um);
				provide(ums);
				
				ScriptSettings.bankItemsToWithdraw[11] = "Smelting urn";
				ScriptSettings.bankItemsToWithdraw[12] = "Strong smelting urn";
				ScriptSettings.bankItemsToWithdraw[13] = "Mining urn";
				ScriptSettings.bankItemsToWithdraw[14] = "Strong mining urn";
				ScriptSettings.bankItemsToWithdraw[15] = "Decorated mining urn";
				ScriptSettings.bankItemsToIgnore[11] = 20402;
				ScriptSettings.bankItemsToIgnore[12] = 20401;
				ScriptSettings.bankItemsToIgnore[13] = 20400;
				ScriptSettings.bankItemsToIgnore[14] = 20406;
				ScriptSettings.bankItemsToIgnore[15] = 20407;
				ScriptSettings.bankItemsToIgnore[16] = 20408;
				ScriptSettings.bankItemsToIgnore[17] = 20394;
				ScriptSettings.bankItemsToIgnore[18] = 20395;
				ScriptSettings.bankItemsToIgnore[19] = 20396;
				ScriptSettings.bankItemsToIgnore[20] = 20286;
				ScriptSettings.bankItemsToIgnore[21] = 20287;
				ScriptSettings.bankItemsToIgnore[22] = 20288;
				ScriptSettings.bankItemsToIgnore[23] = 20293;
				ScriptSettings.bankItemsToIgnore[24] = 20292;
				ScriptSettings.bankItemsToIgnore[25] = 20294;
				log.info("Added urn support!");
			}
			
			if (ScriptSettings.useSuperheat) {
				SuperHeatManager shm = new SuperHeatManager();
				Strategy sh = new Strategy(shm, shm);
				provide(sh);
				ScriptSettings.bankItemsToWithdraw[21] = "Nature rune";
				ScriptSettings.bankItemsToWithdraw[22] = "Gold ore";
				ScriptSettings.bankItemsToIgnore[26] = 561;
				ScriptSettings.bankItemsToIgnore[27] = 444;
				log.info("Added superheat support!");
			}
			
			if (ScriptSettings.useLavaTitan) {
				LavaTitanManager ltm = new LavaTitanManager();
				Strategy lt = new Strategy(ltm, ltm);
				provide(lt);
				ScriptSettings.bankItemsToWithdraw[30] = "Lava titan pouch";
				ScriptSettings.bankItemsToWithdraw[31] = "Summoning potion";
				ScriptSettings.bankItemsToWithdraw[32] = "Obsidian golem pouch";
				ScriptSettings.bankItemsToIgnore[28] = 12788;
				ScriptSettings.bankItemsToIgnore[29] = 12140;
				ScriptSettings.bankItemsToIgnore[30] = 12142;
				ScriptSettings.bankItemsToIgnore[31] = 12144;
				ScriptSettings.bankItemsToIgnore[32] = 12146;
				ScriptSettings.bankItemsToIgnore[33] = 12792;
				log.info("Added lava titan support!");
			}
			
			FailSafe fs = new FailSafe();
			Strategy fss = new Strategy(fs, fs);
			provide(fss);
			
			MoveBox mb = new MoveBox();
			Strategy mbs = new Strategy(mb, mb);
			provide(mbs);
			
			RandomEventGift reg = new RandomEventGift();
			Strategy regs = new Strategy(reg, reg);
			provide(regs);

			switch (loc) {
			case QUARRY:
				
				if (ScriptSettings.mode == Modes.POWERMINING) {
					ScriptSettings.mode = Modes.BANKING;
				}
				
				ScriptSettings.bankItemsToWithdraw[1] = "Waterskin (4)";
				ScriptSettings.bankItemsToWithdraw[2] = "Waterskin (3)";
				ScriptSettings.bankItemsToWithdraw[3] = "Waterskin (2)";
				ScriptSettings.bankItemsToWithdraw[4] = "Waterskin (1)";
				ScriptSettings.bankItemsToWithdraw[5] = "Shantay pass";
				ScriptSettings.bankItemsToIgnore[1] = 1854;
				ScriptSettings.bankItemsToIgnore[2] = 1823;
				ScriptSettings.bankItemsToIgnore[3] = 1825;
				ScriptSettings.bankItemsToIgnore[4] = 1827;
				ScriptSettings.bankItemsToIgnore[5] = 1829;
				log.info("Added waterskin support!");

				Dropper d = new Dropper();
				Strategy ds = new Strategy(d, d);
				provide(ds);

				d.setLock(true);
				d.setSync(false);

				Mining m = new Mining();
				Strategy ms = new Strategy(m, m);
				provide(ms);

				m.setLock(true);
				m.setSync(false);

				Banking ba = new Banking();
				Strategy bs = new Strategy(ba, ba);
				provide(bs);

				WalkToBank wtb = new WalkToBank();
				Strategy wtbs = new Strategy(wtb, wtb);
				provide(wtbs);

				WalkToMine wtm = new WalkToMine();
				Strategy wtms = new Strategy(wtm, wtm);
				provide(wtms);
				break;
			case WILDERNESS_CASTLE:
			case RUNE_ROCKS:
				ScriptSettings.deathWalkPath = deathWalkEdgeToEdge;

				WorldHopper wh = new WorldHopper();
				Strategy worldHopper = new Strategy(wh, wh);
				provide(worldHopper);

				WildernessSafety ws = new WildernessSafety();
				Strategy safety = new Strategy(ws, ws);
				provide(safety);

				if (ScriptSettings.useDeathWalker) {
					DeathWalker dw = new DeathWalker();
					Strategy dws = new Strategy(dw, dw);
					provide(dws);
				}

				ScriptSettings.bankItemsToIgnore[1] = ScriptSettings.foodID;
			default:
				Banking bank = new Banking();
				Mining mining = new Mining();
				WalkToMine mineWalk = new WalkToMine();
				WalkToBank bankWalk = new WalkToBank();

				Strategy mStrat = new Strategy(mining, mining);
				Strategy bStrat = new Strategy(bank, bank);
				Strategy w1 = new Strategy(mineWalk, mineWalk);
				Strategy w2 = new Strategy(bankWalk, bankWalk);

				mStrat.setLock(true);
				mStrat.setSync(false);
				bStrat.setLock(true);
				bStrat.setSync(false);

				provide(mStrat);
				provide(bStrat);
				provide(w1);
				provide(w2);
				break;
			}

			if (ScriptSettings.mode == Modes.POWERMINING && ScriptSettings.location != Locations.QUARRY) {
				PowermineDrop pMiner = new PowermineDrop();
				Dropper dropper = new Dropper();

				Strategy pStrat = new Strategy(pMiner, pMiner);
				Strategy drop = new Strategy(dropper, dropper);

				provide(pStrat);
				drop.setSync(true);
				provide(drop);
				provide(drop);
			}
		}

		public void getItemPrice(int id) {
			log.info("Looking up rock price...");
			GeItem itm = GeItem.lookup(id);
			if (itm != null) {
				log.info("Rock price : " + itm.getPrice());
				ScriptSettings.rockPrice = itm.getPrice();
			} else {
				log.info("Sorry, we could not update the price of the rock!");
			}
		}

		public void setOurLocationTo(Locations loc) {
			switch (loc) {
			case HERO_GUILD:
				ScriptSettings.bankArea = heroesBankArea;
				ScriptSettings.miningArea = heroesArea;
				ScriptSettings.toMinePath = heroesBankToDoor;
				break;
			case DWARF_MINE:
				ScriptSettings.bankArea = sfBankArea;
				ScriptSettings.miningArea = dwarfMineArea;
				ScriptSettings.toMinePath = sfBankToStairs;
				break;
			case CRAFT_GUILD:
				ScriptSettings.bankArea = rimmingtonBankArea;
				ScriptSettings.miningArea = craftGuildArea;
				ScriptSettings.toMinePath = portSarimToCraftGuild;
				break;
			case QUARRY:
				ScriptSettings.bankArea = graniteBankArea;
				ScriptSettings.miningArea = graniteArea;
				ScriptSettings.toMinePath = quarryTravelToMine;
				break;
			case DWARVEN_MINES_RES_DUNGEON:
				ScriptSettings.bankArea = sfBankArea;
				ScriptSettings.toMinePath = sfBankToStairs;
				ScriptSettings.miningArea = dwarvenMineResDungeonArea;
				break;
			case MINING_GUILD_RES_DUNGEON:
				ScriptSettings.bankArea = sfBankArea;
				ScriptSettings.miningArea = miningGuildResDungeonArea;
				ScriptSettings.toMinePath = faladorBankToGuild;
				break;
			case ALKHARID_RES_DUNGEON:
				ScriptSettings.bankArea = akBankArea;
				ScriptSettings.toMinePath = alKharidBankToMine;
				ScriptSettings.miningArea = alKharidResourceDungeonArea;
				break;
			case YANILLE:
				ScriptSettings.bankArea = yanilleBankArea;
				ScriptSettings.miningArea = yanilleMiningArea;
				ScriptSettings.toMinePath = yanilleBankToMine;
				log.info("Yanille paths set.");
				break;
			case ARDOUGNE:
				ScriptSettings.bankArea = ardougneBankArea;
				ScriptSettings.miningArea = ardougneMiningArea;
				ScriptSettings.toMinePath = ardougneBankToMine;
				log.info("Ardougne paths set.");
				break;
			case BARBARIAN_VILLAGE:
				ScriptSettings.bankArea = evBankArea;
				ScriptSettings.miningArea = barbarianMiningArea;
				ScriptSettings.toMinePath = barbarianBankToMine;
				break;
			case AL_KHARID:
				ScriptSettings.toMinePath = alKharidBankToMine;
				ScriptSettings.bankArea = akBankArea;
				ScriptSettings.miningArea = akArea;
				log.info("Al kharid paths set.");
				break;
			case VARROCK_WEST:
				ScriptSettings.toMinePath = vWestToMine;
				ScriptSettings.miningArea = vWestArea;
				ScriptSettings.bankArea = vWestBankArea;
				log.info("Varrock west paths set.");
				break;
			case VARROCK_EAST:
				ScriptSettings.toMinePath = varrockEastBankToMinePaths[Random.nextInt(0, varrockEastBankToMinePaths.length - 1)];
				ScriptSettings.miningArea = vEastArea;
				ScriptSettings.bankArea = vEastBankArea;
				log.info("Varrock east paths set.");
				break;
			case VARROCK_ESSENCE:
				ScriptSettings.toMinePath = vEssence;
				ScriptSettings.bankArea = vEastBankArea;
				ScriptSettings.pitStopAreas = vPitStopArea;
				log.info("Varrock essence paths set");
				break;
			case MAGEGUILD_ESSENCE:
				ScriptSettings.bankArea = yanilleBankArea;
				ScriptSettings.toMinePath = yanilleBankToMageGuild;
				break;
			case RUNE_ROCKS:
				ScriptSettings.bankArea = evBankArea;
				ScriptSettings.miningArea = runeArea;
				ScriptSettings.toMinePath = runeRockPath;
				log.info("Rune rock paths set.");
				break;
			case WILDERNESS_CASTLE:
				ScriptSettings.bankArea = evBankArea;
				ScriptSettings.miningArea = crArea;
				log.info("Wilderness castle paths set.");
				break;
			case MINING_GUILD:
				ScriptSettings.bankArea = sfBankArea;
				ScriptSettings.miningArea = miningGuildArea;
				ScriptSettings.toMinePath = faladorBankToGuild;
				log.info("Mining guild paths set");
				break;
			case SOUTH_LUMBY:
				ScriptSettings.toMinePath = southLumbyToMinePaths[Random.nextInt(0,
						southLumbyToMinePaths.length - 1)];
				ScriptSettings.bankArea = sLBankArea;
				ScriptSettings.miningArea = sLArea;
				break;
			case RIMMINGTON:
				ScriptSettings.bankArea = rimmingtonBankArea;
				ScriptSettings.miningArea = sfArea;
				ScriptSettings.toMinePath = rimmingtonToMine;
				break;
			case LRC:
				ScriptSettings.bankArea = LRCBankArea;
				ScriptSettings.toMinePath = LRCBankToCenter;
				ScriptSettings.miningArea = LRCMiningArea;
				break;
			}
			
			ScriptSettings.reverseToMinePath = Walking.newTilePath(ScriptSettings.toMinePath).reverse();
		}

		public void setOurRock2To(Rocks rock) {
			switch (rock) {
			case GRANITE:
				ScriptSettings.rock2ID = granite;
				ScriptSettings.secondaryRocks = new Integer[] { 10947 };
				log.info("Set our secondary to granite.");
				break;
			case GEM:
				ScriptSettings.rock2ID = diamond;
				ScriptSettings.secondaryRocks = new Integer[] { 11195, 11194, 11364 };
				break;
			case COAL:
				ScriptSettings.rock2ID = coal;
				ScriptSettings.secondaryRocks = new Integer[] { 2096, 2097, 3032,
						3233, 5770, 5771, 5772, 10948, 11930, 11931, 11932, 14850,
						14851, 21287, 21288, 21289, 29215, 29216, 29217, 32426,
						32427, 32428 };
				log.info("Set our rock to coal");
				break;
			case IRON:
				ScriptSettings.rock2ID = iron;
				ScriptSettings.secondaryRocks = new Integer[] { 2092, 2093, 5773,
						5774, 5775, 9717, 9718, 9719, 11954, 11955, 11956, 14099,
						14107, 14913, 14914, 21281, 21282, 21283, 37307, 37308,
						37309, 72083, 72082, 72081 };
				log.info("Set our rock to iron.");
				break;
			case RUNE:
				ScriptSettings.rock2ID = rune;
				ScriptSettings.secondaryRocks = new Integer[] { 14859, 14860,
						33078, 33079, 45069, 45070 };
				log.info("Set our rock to rune");
				break;
			case ADDY:
				ScriptSettings.rock2ID = addy;
				ScriptSettings.secondaryRocks = new Integer[] { 3040, 3273, 5782,
						5783, 11939, 11941, 21275, 21276, 21277, 29233, 29235,
						32425, 32436, 32437 };
				log.info("Set our rock to addy");
				break;
			case MITH:
				ScriptSettings.rock2ID = mith;
				ScriptSettings.secondaryRocks = new Integer[] { 2102, 2103, 3041,
						3280, 5784, 5785, 5786, 11942, 11943, 11944, 21278, 21279,
						21280, 32438, 32439, 32440 };
				log.info("Set our rock to mith");
				break;
			case GOLD:
				ScriptSettings.rock2ID = gold;
				ScriptSettings.secondaryRocks = new Integer[] { 768, 5769, 9720,
						9722, 10574, 10575, 10576, 11183, 11184, 11185, 11943,
						15576, 15577, 15578, 32432, 32433, 32434, 45067, 45068,
						72087, 72088 };
				log.info("Set our rock to gold.");
				break;
			case SILVER:
				ScriptSettings.rock2ID = silver;
				ScriptSettings.secondaryRocks = new Integer[] { 2311, 11186, 11187,
						11188, 11948, 11949, 11950, 15580, 15581, 29224, 29225,
						29226, 32444, 32445, 32446, 37304, 37305, 37306 };
				log.info("Set our rock to silver.");
				break;
			case TIN:
				ScriptSettings.rock2ID = tin;
				ScriptSettings.secondaryRocks = new Integer[] { 2094, 2095, 3038,
						3245, 5776, 5777, 5778, 9714, 9716, 11933, 11934, 11935,
						11957, 11958, 11959, 14902, 14903, 21293, 21294, 21295,
						67009, 67010, 67011, 72094, 72092, 72093 };
				log.info("Set our rock to tin.");
				break;
			case COPPER:
				ScriptSettings.rock2ID = copper;
				ScriptSettings.secondaryRocks = new Integer[] { 2090, 2091, 2097,
						3027, 3229, 5779, 5780, 5781, 9708, 9709, 9710, 11936,
						11937, 11938, 11962, 11961, 11960, 72099, 72098, 72100,
						67014, 67013, 67012 };
				log.info("Set our rock to copper");
				break;
			case PURE_ESSENCE:
				ScriptSettings.rock2ID = 7936;
				ScriptSettings.secondaryRocks = new Integer[] { 4921 };
				log.info("Set our rock to pure essence");
			case CLAY:
				ScriptSettings.rock2ID = clay;
				ScriptSettings.secondaryRocks = new Integer[] { 15504, 15503, 11191, 11189, 11190, 
						15505, 46320, 46322, 46324, 72077, 72075, 72076, 10577,
						10578 };
				log.info("Set our rock to Clay");
				break;
			}
		}

		public void setOurRockTo(Rocks rock) {
			switch (rock) {
			case GRANITE:
				ScriptSettings.rockID = granite;
				ScriptSettings.rocks = new Integer[] { 10947 };
				log.info("Set our rock to granite.");
				break;
			case GEM:
				ScriptSettings.rockID = sapphire;
				ScriptSettings.rocks = new Integer[] { 11364, 11194, 11195 };
				log.info("Set our rock to gems");
				break;
			case COAL:
				ScriptSettings.rockID = coal;
				ScriptSettings.rocks = new Integer[] { 2096, 2097, 3032, 3233,
						5770, 5771, 5772, 10948, 11930, 11931, 11932, 14850, 14851,
						21287, 21288, 21289, 29215, 29216, 29217, 32426, 32427,
						32428, 5999 };
				log.info("Set our rock to coal");
				break;
			case IRON:
				ScriptSettings.rockID = iron;
				ScriptSettings.rocks = new Integer[] { 2092, 2093, 5773, 5774,
						5775, 9717, 9718, 9719, 11954, 11955, 11956, 14099, 14107,
						14913, 14914, 21281, 21282, 21283, 37307, 37308, 37309,
						72083, 72082, 72081 };
				log.info("Set our rock to iron.");
				break;
			case RUNE:
				ScriptSettings.rockID = rune;
				ScriptSettings.rocks = new Integer[] { 14859, 14860, 33078, 33079,
						45069, 45070 };
				log.info("Set our rock to rune");
				break;
			case ADDY:
				ScriptSettings.rockID = addy;
				ScriptSettings.rocks = new Integer[] { 3040, 3273, 5782, 5783,
						11939, 11941, 21275, 21276, 21277, 29233, 29235, 32425,
						32436, 32437 };
				log.info("Set our rock to addy");
				break;
			case MITH:
				ScriptSettings.rockID = mith;
				ScriptSettings.rocks = new Integer[] { 2102, 2103, 3041, 3280,
						5784, 5785, 5786, 11942, 11943, 11944, 21278, 21279, 21280,
						32438, 32439, 32440 };
				log.info("Set our rock to mith");
				break;
			case GOLD:
				ScriptSettings.rockID = gold;
				ScriptSettings.rocks = new Integer[] { 768, 5769, 9720, 9722,
						10574, 10575, 10576, 11183, 11184, 11185, 11943, 15576,
						15577, 15578, 32432, 32433, 32434, 45067, 45068, 72087,
						72088, 45076 };
				log.info("Set our rock to gold.");
				break;
			case SILVER:
				ScriptSettings.rockID = silver;
				ScriptSettings.rocks = new Integer[] { 2311, 11186, 11187, 11188,
						11948, 11949, 11950, 15580, 15581, 29224, 29225, 29226,
						32444, 32445, 32446, 37304, 37305, 37306 };
				log.info("Set our rock to silver.");
				break;
			case TIN:
				ScriptSettings.rockID = tin;
				ScriptSettings.rocks = new Integer[] { 2094, 2095, 3038, 3245,
						5776, 5777, 5778, 9714, 9716, 11933, 11934, 11935, 11957,
						11958, 11959, 14902, 14903, 21293, 21294, 21295, 67009,
						67010, 67011, 72094, 72092, 72093 };
				log.info("Set our rock to tin.");
				break;
			case COPPER:
				ScriptSettings.rockID = copper;
				ScriptSettings.rocks = new Integer[] { 2090, 2091, 2097, 3027,
						3229, 5779, 5780, 5781, 9708, 9709, 9710, 11936, 11937,
						11938, 11962, 11961, 11960, 72099, 72098, 72100, 67014,
						67013, 67012 };
				log.info("Set our rock to copper");
				break;
			case PURE_ESSENCE:
				ScriptSettings.rockID = 7936;
				ScriptSettings.rocks = new Integer[] { 4921 };
				log.info("Set our rock to pure essence");
				break;
			case CLAY:
				ScriptSettings.rockID = clay;
				ScriptSettings.rocks = new Integer[] { 15504, 15503, 15505, 46320,
						46322, 46324, 72077, 72075, 72076, 10577, 10578 };
				log.info("Set our rock to Clay");
				break;
			}
		}

		public void setOurPickaxeTo(Pickaxes pick) {
			switch (pick) {
			case RUNE_PICK:
				ScriptSettings.pickaxe = Pickaxes.RUNE_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Rune pickaxe";
				ScriptSettings.bankItemsToIgnore[0] = 1275;
				log.info("Set Rune pickaxe as our pickaxe.");
				break;
			case ADDY_PICK:
				ScriptSettings.pickaxe = Pickaxes.ADDY_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Adamant Pickaxe";
				log.info("Set Adamant pickaxe as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 1271;
				break;
			case MITH_PICK:
				ScriptSettings.pickaxe = Pickaxes.MITH_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Mithril Pickaxe";
				log.info("Set mithril pickaxe as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 1273;
				break;
			case IRON_PICK:
				ScriptSettings.pickaxe = Pickaxes.IRON_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Iron pickaxe";
				log.info("Set iron pickaxe as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 1267;
				break;
			case STEEL_PICK:
				ScriptSettings.pickaxe = Pickaxes.STEEL_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Steel Pickaxe";
				log.info("Set steel pickaxe as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 1269;
				break;
			case BRONZE_PICK:
				ScriptSettings.pickaxe = Pickaxes.BRONZE_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Bronze pickaxe";
				log.info("Set bronze pickaxe as our pickaxe");
				ScriptSettings.bankItemsToIgnore[0] = 1265;
				break;
			case INFERNO_ADZE:
				ScriptSettings.pickaxe = Pickaxes.INFERNO_ADZE;
				ScriptSettings.bankItemsToWithdraw[0] = "Inferno Adze";
				log.info("Set inferno adze as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 13661;
				break;
			case DRAGON_PICK:
				ScriptSettings.pickaxe = Pickaxes.DRAGON_PICK;
				ScriptSettings.bankItemsToWithdraw[0] = "Dragon pickaxe";
				log.info("Set dragon pickaxe as our pickaxe.");
				ScriptSettings.bankItemsToIgnore[0] = 15259;
				break;
			}
		}

		public void dropAllRows() {
			
			for (int k = 0; k < 4; k++) {
				for (int i = k; i < 29; i += 4) {
					Item it = Inventory.getItemAt(i);
						if (it != null && ignoreableDepositItems.accept(it)) {
							WidgetChild wc = it.getWidgetChild();
							if (wc != null) {
								Mouse.hop(wc.getAbsoluteX(), wc.getAbsoluteY());
								wc.click(false);
								Menu.select("Drop");
							}
						}
				}
			}
		}

		SceneObject curr = null;
		boolean rockInteracted = false;
		public void mineTheNearest(Filter<SceneObject> rocks) {
			
			turnRunOn();
			
			SceneObject nextRock = SceneEntities.getNearest(rocks);

			if (nextRock != null) {
				if (curr == null) {
					curr = nextRock;
					rockInteracted  = false;
					return;
				} else {
					SceneObject checkedRock = SceneEntities.getAt(curr
							.getLocation());
					if (checkedRock != null) {
						if (rocks.accept(checkedRock)) {
							if (!inMiningAnimation()) {
								if (!checkedRock.isOnScreen()) {
									Walking.walk(checkedRock.getLocation());
									Camera.turnTo(checkedRock.getLocation());
									rockInteracted = false;
								}
								if (!rockInteracted) { 
									log.info("Mining checked!");
									checkedRock.interact("Mine");
									rockInteracted = true;
								}
								
							}
						} else {
							curr = nextRock;
							if (!curr.isOnScreen()) {
								Walking.walk(curr.getLocation());
								Camera.turnTo(curr.getLocation());
								rockInteracted = false;
							}
							log.info("Mining current!");
							curr.interact("Mine");
							rockInteracted = true;
						}
					} 
				}
			} else {
				mineTheNearest(secondaryRocks);
			}
		}

		int k = 0;

		public void cameraSweep() {
			Camera.setAngle(k);
			k = 360;
			Camera.setAngle(k);
			if (k == 360) {
				k = 0;
			}
		}

		public boolean inEssenceMine() {
			SceneObject tracks = SceneEntities.getNearest(essenceMineFilter);
			return (tracks != null);
		}

		public boolean isInMageGuild() {
			SceneObject door = SceneEntities.getNearest(mageGuildDoor);
			if (door != null) {
				Tile loc = Players.getLocal().getLocation();
				return (door.getLocation().getX() > loc.getX());
			}
			return false;
		}

		public void usePortal() {
			NPC port = NPCs.getNearest(essencePortals);
			if (port != null) {
				port.interact("Enter");
			}
		}

		public void walkToNearestPortal() {
			NPC port = NPCs.getNearest(essencePortals);
			if (port != null) {
				Camera.turnTo(port.getLocation());
				Camera.setPitch(0);
				Walking.walk(port.getLocation());
			}

		}

		public boolean nearEssencePortal() {
			Tile loc = Players.getLocal().getLocation();
			NPC port = NPCs.getNearest(essencePortals);
			if (port != null) {
				return Calculations.distance(loc, port.getLocation()) <= 5;
			}
			return false;
		}

		public void walkToMageGuild() {
			Walking.newTilePath(yanilleBankToMageGuild).traverse();
		}

		public boolean canSeeAndWalkToNode() {
			SceneObject node = SceneEntities.getNearest(2491);
			if (node != null) {
				Walking.walk(node.getLocation());
				return true;
			}
			return false;
		}

		public boolean nearMageGuildDoor() {
			Tile loc = Players.getLocal().getLocation();
			SceneObject door = SceneEntities.getNearest(1601);
			if (door != null) {
				return Calculations.distance(loc, door.getLocation()) <= 5;
			}
			return false;
		}

		public boolean nearMageGuildNPC() {
			Tile loc = Players.getLocal().getLocation();
			NPC npc = NPCs.getNearest(462);
			if (npc != null) {
				return Calculations.distance(loc, npc.getLocation()) <= 5;
			}
			return false;
		}

		public void mageGuildTeleport() {
			NPC tele = NPCs.getNearest(462);
			if (tele != null) {
				tele.interact("Teleport");
			}
		}

		public void openMageGuildDoor() {
			SceneObject door = SceneEntities.getNearest(mageGuildDoor);
			if (door != null) {
				door.interact("Open");
			} else {

			}
		}

		public boolean nearInnerMine() {
			Tile loc = Players.getLocal().getLocation();
			Tile inner = new Tile(3044, 9735, 0);
			return (Calculations.distance(loc, inner) <= 5);
		}

		public void useTopLadder() {
			SceneObject ldr = SceneEntities.getNearest(2113);
			if (ldr != null) {
				Camera.turnTo(ldr.getLocation());
				ldr.interact("Climb-down");
			}
		}

		public void useBottomLadder() {
			SceneObject ldr = SceneEntities.getNearest(6226);
			if (ldr != null) {
				Camera.turnTo(ldr.getLocation());
				ldr.interact("Climb-up");
			}
		}

		public void walkToFaladorBank() {
			Walking.newTilePath(faladorBankToGuild).reverse().traverse();
		}

		public void walkToInnerMine() {
			Walking.newTilePath(innerGuildToRocks).traverse();
		}

		public void walkToTopLadder() {
			Walking.newTilePath(ScriptSettings.toMinePath).traverse();
		}

		public void walkToBottomLadder() {
			Walking.newTilePath(innerGuildToRocks).reverse().traverse();
		}

		//Dyn

		public Tile getEast(int n) {
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX() + n, loc.getY(), 0);
			return z;
		}

		public Tile  getWest(int n) {
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX() - n, loc.getY(), 0);
			return z;
		}

		public Tile  getNorth(int n) {
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX(), loc.getY() + n, 0);
			return z;
		}

		public Tile  getSouth(int n) {
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX(), loc.getY() - n, 0);
			return z;
		}

		
		//Dyn
		public void moveSouthEast(int n) {
			log.info("Moving Southest " + n +" tiles");
			Tile loc = Players.getLocal().getLocation();
			Walking.walk(new Tile(loc.getX() + n, loc.getY()-n, 0));
		}
		
		//Dyn
		public void moveEast(int n) {
			log.info("Moving east " + n + " tiles");
			Tile loc = Players.getLocal().getLocation();
			Walking.walk(new Tile(loc.getX() + n, loc.getY(), 0));
		}

		//Dyn
		public void moveWest(int n) {
			log.info("Moving west " + n + " tiles");
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX() - n, loc.getY(), 0);
			Walking.walk(z);
		}

		//Dyn
		public void moveNorth(int n) {
			log.info("Moving north " + n + " tiles");
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX(), loc.getY() + n, 0);
			Walking.walk(z);
		}

		//Dyn
		public void moveSouth(int n) {
			log.info("Moving south " + n + " tiles");
			Tile loc = Players.getLocal().getLocation();
			Tile z = new Tile(loc.getX(), loc.getY() - n, 0);
			Walking.walk(z);
		}

		//Dyn
		public boolean npcIsNorth(int n) {
			log.info("Moving north " + n + " tiles");
			if (NPCs.getNearest(n) != null
					&& Players.getLocal().getInteracting().getId() == n) {
				return NPCs.getNearest(n).getLocation().getY() > Players.getLocal()
						.getLocation().getY();
			}
			return false;
		}

		//Dyn
		public boolean npcIsSouth(int id) {
			if (NPCs.getNearest(id) != null
					&& Players.getLocal().getInteracting().getId() == id) {
				return NPCs.getNearest(id).getLocation().getY() < Players
						.getLocal().getLocation().getY();
			}
			return false;
		}

		//Dync
		public boolean npcIsEast(int id) {
			if (NPCs.getNearest(id) != null
					&& Players.getLocal().getInteracting().getId() == id) {
				return NPCs.getNearest(id).getLocation().getX() > Players
						.getLocal().getLocation().getX();
			}
			return false;
		}

		//Dyn
		public boolean npcIsWest(int id) {
			if (NPCs.getNearest(id) != null
					&& Players.getLocal().getInteracting().getId() == id) {
				return NPCs.getNearest(id).getLocation().getX() < Players
						.getLocal().getLocation().getX();
			}
			return false;
		}

		//Dyns
		public boolean spiderIsNorth() {
			if (NPCs.getNearest(63) != null) {
				return NPCs.getNearest(63).getLocation().getY() > Players
						.getLocal().getLocation().getY();
			}
			return false;
		}

		//Dynamic
		public boolean spiderIsSouth() {
			if (NPCs.getNearest(63) != null) {
				return NPCs.getNearest(63).getLocation().getY() < Players
						.getLocal().getLocation().getY();
			}
			return false;
		}

		//Dynamic
		public boolean spiderIsWest() {
			if (NPCs.getNearest(63) != null) {
				return NPCs.getNearest(63).getLocation().getX() < Players
						.getLocal().getLocation().getX();
			}
			return false;
		}

		//Dynamic
		public boolean spiderIsEast() {
			if (NPCs.getNearest(63) != null) {
				return NPCs.getNearest(63).getLocation().getX() > Players
						.getLocal().getLocation().getX();
			}
			return false;
		}

		
		//Dynamic
		public boolean spiderIsAttacking() {
			return (Players.getLocal().isInCombat() && spiderInArea());
		}

		//Dynamic
		public boolean spiderIsAttackingWhileLogout() {
			return (worldHopCondition && Players.getLocal().isInCombat() && spiderInArea());
		}

		//Dynamic
		public boolean spiderInArea() {
			if (NPCs.getNearest(63) != null) {
				Tile spider = NPCs.getNearest(63).getLocation();
				Tile player = Players.getLocal().getLocation();
				return (Calculations.distance(spider, player) <= 1);
			}
			return false;
		}

		//Dynamic
		public boolean healthIsLow() {
			return Players.getLocal().getHpPercent() < 70;
		}

		//Dynamic
		public void eatFood() {
			Item items = Inventory.getItem(ScriptSettings.foodID);
			items.getWidgetChild().interact("Eat");
		}


		//Dynamic
		private boolean playerNearbyDitch() {
			Tile loc = Players.getLocal().getLocation();
			SceneObject ditch = SceneEntities.getNearest(edgeDitchFilter);
			if (ditch != null) {
				return (Calculations.distance(loc, ditch) <= 5);
			}
			return false;
		}

		//Dynamic
		private void crossDitchIfNorth() {
			SceneObject ditch = SceneEntities.getNearest(edgeDitchFilter);
			Tile loc = Players.getLocal().getLocation();
			if (ditch != null && loc.getY() < ditch.getLocation().getY()) {
				Camera.turnTo(ditch.getLocation());
				ditch.interact("Cross");
				crossedNorth = true;
			}
		}

		//Dynamic Computation
		private void crossDitchIfSouth() {
			SceneObject ditch = SceneEntities.getNearest(edgeDitchFilter);
			Tile loc = Players.getLocal().getLocation();
			if (ditch != null && loc.getY() > ditch.getLocation().getY()) {
				Camera.turnTo(ditch.getLocation());
				ditch.interact("Cross");
				crossedSouth = true;
			}
		}

		//Dynamic computation
		private void restIfLowEnergy() {
			if (Walking.getEnergy() < 90 && nearbyMusician()) {
				NPC rest = NPCs.getNearest(8715);
				if (rest != null) {
					Camera.turnTo(rest.getLocation());
					rest.interact("Listen-to");
					Time.sleep(10000);
					if (Walking.getEnergy() > 90) {
						passedRestStop = true;
					}
				}
			}
		}

		//Dynamic computation
		private boolean nearbyMusician() {
			Tile loc = Players.getLocal().getLocation();
			NPC rest = NPCs.getNearest(8715);
			if (rest != null) {
				return (Calculations.distance(loc, rest) <= 5);
			}
			return false;
		}
		
		public void miningAlgorithm() {
			if (hasSuperHeatItems()) {
				if (havePrimaryRequirement()) {
					ScriptSettings.status = "Mining secondary";
					mineTheNearest(secondaryRocks);
				} else {
					ScriptSettings.status = "Mining primary.";
					mineTheNearest(selectedRocks);
				}
			} else {
				mineTheNearest(selectedRocks);
			}
		}
		
		public void drawMiningProgress(Graphics2D g, int x, int y) {
			g.setColor(Color.darkGray);
			g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.fillRoundRect(x, y,
					(int) (PBAR_WIDTH * (miningPercentToLevel() / 100)),
					PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.setColor(Color.black);
			g.drawString("Mining: " + miningPercentToLevel() + "%", x + 55, y + 15);
		}

		public void drawSmithingProgress(Graphics2D g, int x, int y) {
			g.setColor(Color.darkGray);
			g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.fillRoundRect(x, y,
					(int) (PBAR_WIDTH * (smithingPercentToLevel() / 100)),
					PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.setColor(Color.black);
			g.drawString("Smithing: " + smithingPercentToLevel() + "%", x + 55,
					y + 15);
		}

		public void drawMagicProgress(Graphics2D g, int x, int y) {
			g.setColor(Color.darkGray);
			g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.fillRoundRect(x, y,
					(int) (PBAR_WIDTH * (magicPercentToLevel() / 100)),
					PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.setColor(Color.black);
			g.drawString("Magic: " + magicPercentToLevel() + "%", x + 55, y + 15);
		}

		public void drawHideBox(Graphics2D g, int x, int y) {
			HIDEBOX_X = x + 521;
			HIDEBOX_Y = y - 52;

			g.setColor(Color.red);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
			g.fillRoundRect(x + 521, y + 15, 20, (RECT_HEIGHT + 3) / 2, 5, 5);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.setColor(Color.green);

		}

		public void drawHide(Graphics2D g) {
			g.setColor(Color.black);
			g.translate(HIDEBOX_X + 5, HIDEBOX_Y + 85);
			g.rotate(Math.PI / 2);
			g.setFont(new Font("Arial", 10, 15));
			g.drawString("Hide", 0, 0);
		}

		public void drawMove(Graphics2D g) {
			g.drawString("Move", -65, 0);
			
		}

		public void drawTotalGold(Graphics2D g, String totalGold, int x, int y) {
			g.drawString("Total Gold: " + totalGold, x + 455, y + 24);
		}

		public void drawTotalMining(Graphics2D g, String totalMining, int x, int y) {
			g.drawString("Total MiningXp: " + totalMining, x + 455, y + 24);
		}

		public void drawTotalMagic(Graphics2D g, String totalMagic, int x, int y) {
			g.drawString("Total MagicXp: " + totalMagic, x + 455, y + 24);
		}

		public void drawTotalSmithing(Graphics2D g, String totalSmithing, int x,
				int y) {
			g.drawString("Total SmithingXp: " + totalSmithing, x + 455, y + 24);
		}

		public void drawPickaxe(Graphics2D g, String pickaxe, int x, int y) {
			g.drawString("Pickaxe: " + pickaxe, x + 455, y + 45);
		}

		public void drawMode(Graphics2D g, String mode, int x, int y) {
			g.drawString("Mode: " + mode, x + 455, y + 45);
		}

		public void drawStatus(Graphics2D g, String status, int x, int y) {
			g.drawString("Status: " + status, x, y);
		}

		public void drawLocation(Graphics2D g, String location, int x, int y) {
			drawHisLocation(g, x + 375, y + 5);
			g.drawString("Location: " + location, x + 455, y + 45);
		}

		public void drawHisLocation(Graphics2D g, int x, int y) {
			g.drawImage(hisLocation, x, y, null);
		}

		public void drawMiningXpPerHour(Graphics2D g, String xpPerHour, int x, int y) {
			drawHisMiningXp(g, x + 375, y + 5);
			g.drawString("Mining: " + xpPerHour, x + 455, y + 45);
		}

		public void drawMagicXpPerHour(Graphics2D g, String xpPerHour, int x, int y) {
			drawHisMagicXp(g, x + 375, y + 5);
			g.drawString("Magic: " + xpPerHour, x + 455, y + 45);
		}

		public void drawSmithingXpPerHour(Graphics2D g, String xpPerHour, int x,
				int y) {
			drawHisSmithingXp(g, x + 375, y + 5);
			g.drawString("Smithing: " + xpPerHour, x + 455, y + 45);
		}

		public void drawHisMagicXp(Graphics2D g, int x, int y) {
			g.drawImage(hisMagic, x, y, null);
		}

		public void drawHisSmithingXp(Graphics2D g, int x, int y) {
			g.drawImage(hisSmithing, x, y, null);
		}

		public void drawGoldPerHour(Graphics2D g, String goldPerHour, int x, int y) {
			drawHisGold(g, x + 250, y);
			g.drawString("Gold: " + goldPerHour, x + 455, y + 45);
		}

		public void drawRunTime(Graphics2D g, String runTime, int x, int y) {
			drawHisTimer(g, x + 125, y - 12);
			g.drawString("Time: " + runTime, x + 455, y + 45);
		}

		public void drawTheKing(Graphics2D g, int x, int y) {
			g.drawImage(theKing, x, y, null);
		}

		public void drawHisGold(Graphics2D g, int x, int y) {
			g.drawImage(hisGold, x, y, null);
		}

		public void drawHisMiningXp(Graphics2D g, int x, int y) {
			g.drawImage(hisXp, x, y, null);
		}

		public void drawHisLogo(Graphics2D g, int x, int y) {
			g.drawImage(hisLogo, x, y, null);
		}

		public void drawHisTimer(Graphics2D g, int x, int y) {
			g.drawImage(hisTimer, x, y, null);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}
		

		public String timeFormat() {
			Long currTime = System.currentTimeMillis();

			long aMs = currTime - runTimeMs;

			String hours = (int) (aMs / 3600000) % 24 + "";
			String minutes = (int) (aMs / 60000) % 60 + "";
			String seconds = (int) (aMs / 1000) % 60 + "";

			if (hours.length() == 1) {
				hours = "0" + hours;
			}

			if (minutes.length() == 1) {
				minutes = "0" + minutes;
			}

			if (seconds.length() == 1) {
				seconds = "0" + seconds;
			}

			return hours + ":" + minutes + ":" + seconds;
		}

		public String xpPerHour() {
			int currentXP = Skills.getExperience(Skills.MINING);
			int totalXp = currentXP - startXP;
			Long currTime = System.currentTimeMillis();
			Long overall = currTime - runTimeMs;
			double seconds = (double) ((overall / 1000));
			if (seconds != 0) {
				float xpPerHour = (float) (totalXp / seconds);
				xpPerHour = xpPerHour * 60 * 60;
				return xpPerHour + "";
			}
			return "";
		}

		public String gpPerHour() {
			int gold = ScriptSettings.oresMined * ScriptSettings.rockPrice;
			double seconds = (double) (((System.currentTimeMillis() - runTimeMs) / 1000));
			if (seconds != 0) {
				float gpPerHour = (float) (gold / seconds);

				gpPerHour = gpPerHour * 60 * 60;
				return gpPerHour + "";
			}
			return "";
		}

		public String smithingXpPerHour() {
			int currentXP = Skills.getExperience(Skills.SMITHING);
			int totalXp = currentXP - smithingStartXP;
			Long currTime = System.currentTimeMillis();
			Long overall = currTime - runTimeMs;
			double seconds = (double) ((overall / 1000));
			if (seconds != 0) {
				float xpPerHour = (float) (totalXp / seconds);
				xpPerHour = xpPerHour * 60 * 60;
				return xpPerHour + "";
			}
			return "";
		}

		public String magicXpPerHour() {
			int currentXP = Skills.getExperience(Skills.MAGIC);
			int totalXp = currentXP - magicStartXP;
			Long currTime = System.currentTimeMillis();
			Long overall = currTime - runTimeMs;
			double seconds = (double) ((overall / 1000));
			if (seconds != 0) {
				float xpPerHour = (float) (totalXp / seconds);
				xpPerHour = xpPerHour * 60 * 60;
				return xpPerHour + "";
			}
			return "";
		}

		public String totalGold() {
			return ScriptSettings.totalGold + "";
		}

		public String totalMiningXp() {
			int curr = Skills.getExperience(Skills.MINING);
			return curr - startXP + "";
		}

		public String totalSmithingXp() {
			int curr = Skills.getExperience(Skills.SMITHING);
			return curr - smithingStartXP + "";
		}

		public String totalMagicXp() {
			int curr = Skills.getExperience(Skills.MAGIC);
			return curr - magicStartXP + "";
		}

		public double miningPercentToLevel() {
			int base = Skills.XP_TABLE[Skills.getLevel(Skills.MINING)];
			int roof = Skills.XP_TABLE[Skills.getLevel(Skills.MINING) + 1];
			int curr = Skills.getExperience(Skills.MINING);

			double gap = roof - base;
			double real = curr - base;

			return (int) ((real / gap) * 100);

		}

		public double smithingPercentToLevel() {
			int base = Skills.XP_TABLE[Skills.getLevel(Skills.SMITHING)];
			int roof = Skills.XP_TABLE[Skills.getLevel(Skills.SMITHING) + 1];
			int curr = Skills.getExperience(Skills.SMITHING);

			double gap = roof - base;
			double real = curr - base;

			return (int) ((real / gap) * 100);

		}

		public double magicPercentToLevel() {
			int base = Skills.XP_TABLE[Skills.getLevel(Skills.MAGIC)];
			int roof = Skills.XP_TABLE[Skills.getLevel(Skills.MAGIC) + 1];
			int curr = Skills.getExperience(Skills.MAGIC);

			double gap = roof - base;
			double real = curr - base;

			return (int) ((real / gap) * 100);

		}

		@Override
		public void onRepaint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			drawMiningTile(g2d);
			drawMouseCursor(g2d);
			movePaintBox(g2d, PAINT_X, PAINT_Y);
		}

		public void drawMiningTile(Graphics2D g) {
			if (curr != null) {
				g.setColor(Color.red);
				curr.draw(g);
				g.setColor(Color.black);
				g.drawString("Target", (int)curr.getCentralPoint().getX()-15, (int)curr.getCentralPoint().getY()-15);
			}
		}

		public void drawMouseCursor(Graphics2D g) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			g.setColor(Color.black);
			g.drawLine(0, y, 800, y);
			g.drawLine(x, 0, x, 800);
		}

		public void movePaintBox(Graphics2D g, int x, int y) {

			if (guiDone) {
				if (!hideBox) {
					g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
					drawRectangle(g, x, y);
					g.setColor(Color.BLACK);
					drawTheKing(g, x - 95, y - 5);
					drawHisLogo(g, x - 50, y - 90);
					drawStatus(g, ScriptSettings.status, x + 15, y + 32);

					drawRunTime(g, timeFormat(), x - 390, y + 43);
					drawMode(g, ScriptSettings.mode.toString(), x - 390, y + 53);
					drawLocation(g, ScriptSettings.location.toString(), x - 390,
							y + 63);
					drawPickaxe(g, ScriptSettings.pickaxe.toString(), x - 390,
							y + 73);

					drawGoldPerHour(g, gpPerHour() + "gp/hr", x - 230, y + 43);
					drawMiningXpPerHour(g, xpPerHour() + "xp/hr", x - 230, y + 53);
					drawSmithingXpPerHour(g, smithingXpPerHour() + "xp/hr",
							x - 230, y + 63);
					drawMagicXpPerHour(g, magicXpPerHour() + "xp/hr", x - 230,
							y + 73);

					drawTotalGold(g, totalGold(), x - 110, y + 63);
					drawTotalMining(g, totalMiningXp(), x - 110, y + 73);
					drawTotalSmithing(g, totalSmithingXp(), x - 110, y + 83);
					drawTotalMagic(g, totalMagicXp(), x - 110, y + 93);

					drawSmithingProgress(g, x + 180, y + PBAR_HEIGHT - 10);
					drawMiningProgress(g, x + 180, y + 2 * PBAR_HEIGHT - 10);
					drawMagicProgress(g, x + 180, y + 3 * PBAR_HEIGHT - 10);
				}
				drawMoveBox(g, x - 30, y + 50);
				drawHideBox(g, x - 30, y + 50);
				drawHide(g);
				drawMove(g);
			}
		}

		public void drawRectangle(Graphics2D g, int x, int y) {
			Color green = Color.GREEN;
			g.setColor(green);
			g.drawRoundRect(x, y, RECT_WIDTH, RECT_HEIGHT, 10, 10);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f));
			g.fillRoundRect(x, y, RECT_WIDTH, RECT_HEIGHT, 10, 10);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}


		public void drawMoveBox(Graphics2D g, int x, int y) {
			MOVEBOX_X = x + 521;
			MOVEBOX_Y = y - 52;

			g.setColor(Color.red);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
			g.fillRoundRect(x + 521, y - 52, 20, (RECT_HEIGHT + 3) / 2, 5, 5);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.setColor(Color.green);

		}
	// *****************************************************************************************************************
	/* Setup */
	// *****************************************************************************************************************
	@Override
	protected void setup() {
		log.info("Attempting to load all images..");
		theKing = getImage("http://i49.tinypic.com/t551xl.gif")
				.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		hisLogo = getImage("http://i47.tinypic.com/286tr3o.gif");
		log.info("Successfully loaded all images.");
		log.info("KingMiner initiated.");
		log.info("KingGUI initiated.");
		SpiffyGUI gui = new SpiffyGUI();
		gui.setVisible(true);
		gui.setResizable(false);
		log.info("MiningScript initiated.");
		log.info("Setting up data.");
		runTimeMs = System.currentTimeMillis();
		startXP = Skills.getExperience(Skills.MINING);
		smithingStartXP = Skills.getExperience(Skills.SMITHING);
		magicStartXP = Skills.getExperience(Skills.MAGIC);
		log.info("Starting irc client..");
	}


	// *****************************************************************************************************************
	/* MiningScript */
	// *****************************************************************************************************************
	
	
	Long goldCheckTimer = 0L;
	SceneObject nextRock = null;

	static int PAINT_X = 8;
	static int PAINT_Y = 342;
	static int RECT_HEIGHT = 128;
	static int RECT_WIDTH = 510;
	static int MOVEBOX_X;
	static int MOVEBOX_Y;
	static int HIDEBOX_X;
	static int HIDEBOX_Y;
	static int PBAR_HEIGHT = 20;
	static int PBAR_WIDTH = 300;
	static int PBAR_OVAL = 5;
	
	boolean hideBox = false;
	boolean LRCDepositingState = false;
	boolean goldCheckTimerSet = false;
	boolean worldHopCondition = false;
	boolean boughtMithPick = false;
	boolean boughtAddyPick = false;
	boolean boughtRunePick = false;
	boolean moveBox = false;
	boolean returnToBank = false;
	boolean crossedSouth = false;
	boolean usedBtmLadder = false;
	boolean usedPortal = false;
	boolean usedDoor = false;
	boolean travelledToShantay = false;
	boolean usedShantayPass = false;
	boolean beenOutside = false;
	boolean walkedToInner = false;
	boolean crossedNorth = false;
	boolean passedRestStop = false;
	boolean runningFromCombat = false;
	boolean changingWorlds = false;
	boolean usedTopLadder = false;
	boolean walkingToInnerMine = false;
	boolean downStairs = false;
	boolean walkedThroughPass = false;
	boolean travelledToQuarry = false;
	boolean clickedBuy = false;
	
	Point lastMousePoint = null;
	int[] hopPoints = new int[] { 0, 4, 8, 12, 16 };
	
	
	/**
	 * 
	 * Banking class
	 * 
	 *Author:KingJelly
	 *
	 */
	private class Banking extends Strategy implements Task {
		@Override
		public boolean validate() {
		
			Player p = Players.getLocal();
			int invCount = Inventory.getCount();
			int ignoreableCount = Inventory.getCount(ignoreableDepositItems);
			int foodCount = Inventory.getCount(ScriptSettings.foodID);
			int foodAmt = ScriptSettings.foodWithdrawAmount;
			boolean bankContains = ScriptSettings.bankArea.contains(p);
			
			
			if (guiDone && ScriptSettings.mode == Modes.BANKING) {

				if (bankContains) {

					switch (ScriptSettings.location) {
					case RUNE_ROCKS:
						return ignoreableCount != 0 || foodCount < foodAmt;
					case QUARRY:
						return (Inventory.getCount(waterskins) < 10 || ScriptSettings.wearingEnchantedTiara);
					case DWARVEN_MINES_RES_DUNGEON:
						return invCount == 28|| nearDepositBox();
					default:
						return (ignoreableCount  != 0);
					}
				}
			}
			return false;
		}

		@Override
		public void run() {
			
			ScriptSettings.walking = false;
			ScriptSettings.status = "Banking";
			switch (ScriptSettings.location) {
			case CRAFT_GUILD:
			case RIMMINGTON:
				depositAllAtRimmington();
				break;
			case QUARRY:
				quarryWithdrawWaterskins();
				break;
			case LRC:
				LRCdepositAllExceptIgnoreables();
				break;
			case DWARVEN_MINES_RES_DUNGEON:
				DMineDepositAllExceptIgnoreables();
				break;
			default:
				diedRecently = false;
				returnToBank = false;
				if (!Bank.isOpen()) {
					Bank.open();
				}
				depositAllExceptIgnoreables();
				withdrawAllWithdrawables();
				break;
			}
		}
	}

	/**
	 * 
	 * Used to prevent recomputation in validation methods that are constantly recomputed.
	 * 
	 * Author:KingJelly
	 *
	 */
	private class ComputeOnce extends Strategy implements Task {

		boolean computedOnce = false;
		
		@Override
		public void run() {
			
			if (guiDone) {
				log.info("Computing this once!!");
				ScriptSettings.reverseToMinePath = Walking.newTilePath(ScriptSettings.toMinePath).reverse();
				ScriptSettings.hasPickaxeInInvOrEquip = detectedPickaxe();
				ScriptSettings.wearingEnchantedTiara = wearingEnchantedTiara();
				ScriptSettings.canCastSuperheat = canCastSuperheat();
				computedOnce = true;
			}
			
		}
		
		@Override
		public boolean validate() {
			return (guiDone && Game.isLoggedIn() && !computedOnce);
		}
	}
	
	/**
	 * 
	 * Mining class
	 * 
	 * Author:KingJelly
	 *
	 */
	private class Mining extends Strategy implements Task {

		
		Tile loc;
		boolean miningAreaContains;
		boolean invCount;
		boolean invCount2;
		@Override
		public boolean validate() {
			if (guiDone && ScriptSettings.mode != Modes.POWERMINING && ScriptSettings.mode != Modes.ADVENTURE) {
				
				loc                = Players.getLocal().getLocation();
				miningAreaContains = ScriptSettings.miningArea.contains(loc);
				invCount           = Inventory.getCount() < 28;
				invCount2          = Inventory.getCount(ignoreableDepositItems) == 0;
				
				switch (ScriptSettings.location) {
				case QUARRY:
					return Inventory.getCount(waterskins) > 0 && invCount && miningAreaContains;
				case MAGEGUILD_ESSENCE:
					return (invCount && nrObj(2491, loc));
				case MINING_GUILD:
					if (walkingToInnerMine) {
						break;
					}
					return !worldHopCondition && miningAreaContains && invCount;
				case VARROCK_ESSENCE:
					return nrObj(2491, loc);
				default:
					return !worldHopCondition && miningAreaContains && invCount;
				}
			}
			return false;
		}

		@Override
		public void run() {
			
			ScriptSettings.walking = false;
			ScriptSettings.status = "Mining";
			worldHopCondition = false;

			
			
			switch (ScriptSettings.location) {
			case MAGEGUILD_ESSENCE:
			case VARROCK_ESSENCE:

				if (nrObj(2491, loc)) {
					mineTheNearest(essenceRock);
				} else {
					moveSouthEast(8);
				}
				break;
			case DWARVEN_MINES_RES_DUNGEON:
				if (nrObjs(depoBoxes, loc) && invCount2) {
					moveEast(15);
				}
				miningAlgorithm();
				break;
			default:
				miningAlgorithm();
			}
		}
	}
	
	/**
	 * 
	 * Class for walking to the bank.
	 * 
	 * Author:KingJelly
	 *
	 */
	private class WalkToBank extends Strategy implements Task {

		@Override
		public boolean validate() {
			
			Tile loc = Players.getLocal().getLocation();
			boolean b = ScriptSettings.bankArea.contains(Players.getLocal().getLocation());
			boolean z = Inventory.getCount() == 28;
			
			if (guiDone
					&& (ScriptSettings.mode != Modes.POWERMINING)) {
				switch (ScriptSettings.location) {
				case QUARRY:
					return (Inventory.getCount(waterskins) == 0 && !wearingEnchantedTiara()) && !b;
				case MINING_GUILD_RES_DUNGEON:
					return z
							&& !b;
				case ALKHARID_RES_DUNGEON:
					return z
							&& !b;
				case DWARVEN_MINES_RES_DUNGEON:
					return z && ScriptSettings.miningArea.contains(Players.getLocal().getLocation()) && !nrObjs(depoBoxes, loc);
				case MAGEGUILD_ESSENCE:
					return z
							&& !b;
				case VARROCK_ESSENCE:
					return !b && Inventory.getCount() == 28;
				case RUNE_ROCKS:
					return (healthIsLow())
							|| z;
				default:
					if (Tabs.getCurrent() != Tabs.INVENTORY) {
						Tabs.INVENTORY.open();
					}
					
					return (!b
							&& z) || ((Inventory
									.getCount() - Inventory
									.getCount(ScriptSettings.foodID)) > 20 && ScriptSettings.location == Locations.RUNE_ROCKS)
							&& (ScriptSettings.mode == Modes.BANKING || (ScriptSettings.mode == Modes.POWERMINING && ScriptSettings.bankGems)) && !worldHopCondition;
				}
			}
			return false;
		}

		@Override
		public void run() {
			
			ScriptSettings.walking = true;
			
			ScriptSettings.status = "Walking to bank";
			Tile p = Players.getLocal().getLocation();
			boolean mineAreaContains = ScriptSettings.miningArea.contains(p);
			boolean bankAreaContains = ScriptSettings.bankArea.contains(p);
			
			
			if (!ScriptSettings.bankArea.contains(p)) {
				switch (ScriptSettings.location) {
				case LRC:
					
					break;
				case HERO_GUILD:
					
					boolean heroesGuildBottomLadder = nrObj(67691, p);
					boolean heroesGuildUpperLadder = nrObj(67690, p);
					
					if (!heroesGuildBottomLadder && (heroesArea.contains(p) || downstairsHeroesArea.contains(p))) {
						walkToLadderFromHeroes();
					}
					
					if (heroesGuildBottomLadder) {
						intrObj(67691, "Climb-up");
					}
					
					if (!mineAreaContains && heroesGuildUpperLadder && !beenOutside) {
						intrObj(2625, "Open");
						
					}
					
					if (outsideHeroes.contains(p)) {
						beenOutside = true;
					}
					
					
					if (!mineAreaContains && !heroesGuild.contains(p) && beenOutside) {
						ScriptSettings.reverseToMinePath.traverse();
					}
					break;
				case CRAFT_GUILD:
					
					if (nrObj(2647, p) && mineAreaContains) {
						intrObj(2647, "Open");
					}
					
					walkToPortSarimDepo();
					
					break;
				case QUARRY:
					if (!shantayArea.contains(p) && !travelledToShantay) {
						ScriptSettings.status = "Walking to desert camp.";
						walkToDesertCamp();
					}
					
					if (shantayArea.contains(p) && !travelledToShantay) {
						ScriptSettings.status = "Travelling on carpet.";
						travelToShantayPass();
						travelledToShantay = true;
					}
					
					if (!bankAreaContains && nearShantayPass()) {
						goThroughShantayPass();
						usedShantayPass = true;
					}
					break;
				case MINING_GUILD_RES_DUNGEON:
					if (mineAreaContains) {
						intrObjs(resourceDungeons, "Exit");
					}

					if (!nrObj(6226, p) && !usedBtmLadder) {
						log.info("Walking to bottom ladder.");
						walkToBottomLadder();
						usedTopLadder = false;
					}

					if (nrObj(6226, p)) {
						intrObj(6226, "Climb-up");
						usedBtmLadder = true;
					}

					if (!bankAreaContains && usedBtmLadder) {
						walkToFaladorBank();
					}
					break;
				case ALKHARID_RES_DUNGEON:

					if (mineAreaContains) {
						intrObjs(resourceDungeons, "Exit");
					}

					if (!mineAreaContains) {
						ScriptSettings.reverseToMinePath.traverse();
					}

					break;
				case DWARF_MINE:
					if (!nrObj(30943, p) && mineAreaContains) {
						intrObj(30943, "Climb-up");
					}
					
					if (nrObj(11714, p)) {
						intrObj(11714, "Open");
					}
					
					if (!mineAreaContains) {
						ScriptSettings.reverseToMinePath.traverse();
					}
					break;
				case DWARVEN_MINES_RES_DUNGEON:
					break;
				case MINING_GUILD:
					if (!nrObj(6226, p) && !usedBtmLadder) {
						log.info("Walking to bottom ladder.");
						walkToBottomLadder();
						usedTopLadder = false;
					}
					
					if (nrObj(6226, p)) {
						useBottomLadder();
						usedBtmLadder = true;
					}

					if (!bankAreaContains && usedBtmLadder) {
						walkToFaladorBank();
					}
					break;
				case MAGEGUILD_ESSENCE:

					if (!nearEssencePortal() && !isInMageGuild()) {
						log.info("Walking to nearest portal.");
						walkToNearestPortal();
					}

					if (nearEssencePortal()) {
						log.info("Using portal.");
						usePortal();
					}
					if (isInMageGuild()) {
						log.info("Walking to door.");
						Walking.walk(new Tile(2596, 3087, 0));
						openMageGuildDoor();
					}

					if (!isInMageGuild()) {
						Walking.newTilePath(yanilleBankToMageGuild).reverse()
								.traverse();
					}

					break;
				case VARROCK_ESSENCE:
					log.info("Walking to varrock east.");
					NPC port = NPCs.getNearest(essencePortals);
					if (port != null) {
						Tile portal = port.getLocation();
						double d = Calculations.distance(p, portal);
						if (d > 5 && d < 50) {
							log.info("Walking to portal.");
							Walking.walk(portal);
						}
						if (port.validate()) {
							portal.interact("Enter");
						}
					}
					if (!bankAreaContains) {
						Walking.newTilePath(vEssence).reverse().traverse();
					}
					break;
				case RUNE_ROCKS:
					returnToBank = true;

					if (!restStopArea
							.contains(p)
							&& !passedRestStop) {
						Walking.newTilePath(restStopToRuneRocks).reverse()
								.traverse();
					}

					if (restStopArea.contains(p)
							&& nearbyMusician()) {
						restIfLowEnergy();
					}

					if (!playerNearbyDitch() && passedRestStop) {
						Walking.newTilePath(ditchToRestStop).reverse()
								.traverse();
					}

					if (playerNearbyDitch() && !crossedSouth) {
						crossDitchIfSouth();
					}
					break;
				case WILDERNESS_CASTLE:
					break;
				default:
					ScriptSettings.reverseToMinePath.traverse();
				}
			}
		}
	}
	
	/** 
	 * 
	 * Class for walking to the mine
	 * 
	 * Author:KingJelly
	 *
	 */
	private class WalkToMine extends Strategy implements Task {

		Tile p;
		boolean b;
		boolean c;
		
		@Override
		public boolean validate() {

			p = Players.getLocal().getLocation();
			b = Inventory.getCount(ignoreableDepositItems) == 0;
			c = ScriptSettings.miningArea.contains(p);

			
			if (guiDone && ScriptSettings.mode != Modes.ADVENTURE) {
				switch (ScriptSettings.location) {
				case QUARRY:
					return Inventory.getCount(waterskins) > 0 && b && !c;
				case MINING_GUILD_RES_DUNGEON:
					return !c && b;
				case DWARVEN_MINES_RES_DUNGEON:
					return (!c && Inventory.getCount(ignoreableDepositItems) < 28);
				case ALKHARID_RES_DUNGEON:
					return !c && b;
				case MINING_GUILD:
					return (b && !c);
				case VARROCK_ESSENCE:
					return b
							&& NPCs.getNearest(24381) == null
							&& SceneEntities.getNearest(essenceMineFilter) == null;
				case MAGEGUILD_ESSENCE:
					return b && !nrObj(2491, p);
				case RUNE_ROCKS:
					if (ScriptSettings.miningArea.contains(p)) {
						passedRestStop = false;
					}
					return !worldHopCondition
							&& !c
							&& (b && Inventory.getCount(ScriptSettings.foodID) >= ScriptSettings.foodWithdrawAmount)
							&& !returnToBank;
				case WILDERNESS_CASTLE:
					return false;
				default:
					return ((!c && b));
				}
			}
			return false;
		}

		@Override
		public void run() {
			
			ScriptSettings.status = "Walking to mine.";
			ScriptSettings.walking = true;
			
			switch (ScriptSettings.location) {
			case HERO_GUILD:
				if (!nrObj(2625, p) && !downstairsHeroesArea.contains(p)) {
					ScriptSettings.status = "Walking to door.";
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();
				}

				if (nrObj(2625, p) && !heroesGuild.contains(p)) {
					ScriptSettings.status = "Clicking on door.";
					intrObj(2625, "Open");
				}

				if (heroesGuild.contains(p)) {
					intrObj(67690, "Climb-down");
				}

				if (downstairsHeroesArea.contains(p)) {
					ScriptSettings.status = "Walking to inner heroes guild.";
					walkToInnerHeroesGuild();
				}

				break;
			case CRAFT_GUILD:
				if (!nrObj(2647, p)) {
					ScriptSettings.status = "Walking to door.";
					walkToCraftGuild();
				}

				if (nrObj(2647, p)) {
					ScriptSettings.status = "Opening door.";
					intrObj(2647, "Open");
				}
				break;
			case QUARRY:
				ScriptSettings.status = "Buying Shantay pass";
				if (!clickedBuy && !haveShantayPass() && !walkedThroughPass) {
					buyShantayPass();
					clickedBuy = true;
				}
				ScriptSettings.status = "Walking through pass";
				if (haveShantayPass() && !walkedThroughPass) {
					goThroughShantayPass();
					walkedThroughPass = true;
				}

				ScriptSettings.status = "Travelling on carpet";
				if (walkedThroughPass && !travelledToQuarry) {
					travelToDesertCamp();
					travelledToQuarry = true;
				}

				if (travelledToQuarry && !c) {
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();

				}
				break;
			case MINING_GUILD_RES_DUNGEON:
				if (!nrObj(2113, p) && !usedTopLadder) {
					walkToTopLadder();
					usedBtmLadder = false;
				}

				if (nrObj(2113, p)) {
					useTopLadder();
					usedTopLadder = true;
				}

				if (usedTopLadder) {
					intrObjs(resourceDungeons, "Enter");
					usedTopLadder = false;
				}
				break;
			case DWARF_MINE:
				if (!nrObj(11714, p)) {
					ScriptSettings.status = "Walking to door.";
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();
				}

				if (nrObj(11714, p) && !nrObj(30944, p)) {
					ScriptSettings.status = "Opening stair door.";
					intrObj(11714, "Open");
				}
				if (nrObj(30944, p) && !downStairs) {
					ScriptSettings.status = "Walking down stairs";
					walkToDownStairs();
				}

				if (nrObj(30944, p) && !c) {
					ScriptSettings.status = "Clicking on stairs.";
					intrObj(30944, "Climb-down");
				}

				if (!c) {
					Walking.newTilePath(stairsToDung).traverse();
				}
				break;
			case DWARVEN_MINES_RES_DUNGEON:

				final boolean nrDown = nrObj(30944, p);
				final boolean nrDoor = nrObj(11714, p);
				final boolean nrUp   = nrObj(30943, p);
				
				if (nrDoor) {
					intrObj(11714, "Open");
				}
				if (!nrDown && !downStairs) {
					walkToDownStairs();
				}

				if (nrDown && !downStairs) {
					intrObj(30944, "Climb-down");
					if (nrUp) {
						downStairs = true;
					}
				}

				if (!c && downStairs) {
					Walking.newTilePath(stairsToDung).traverse();
				}

				if (nrObjs(resourceDungeons, p)) {
					intrObjs(resourceDungeons, "Enter");
				}

				break;
			case ALKHARID_RES_DUNGEON:
				
				final boolean nrDoorz = nrObjs(resourceDungeons, p);
				
				if (!nrDoorz && !c) {
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();
				}

				if (nrDoorz) {
					intrObjs(resourceDungeons, "Enter");
				}

				break;
			case VARROCK_ESSENCE:
				
				final boolean pitStopContain = ScriptSettings.pitStopAreas.contains(p);
				
				if (!pitStopContain) {
					Walking.newTilePath(vEssence).traverse();
				}
				else {
					intrObj(24381, "Open");
					intrNPC(5913, "Teleport");
				}
				break;
			case MAGEGUILD_ESSENCE:
				usedDoor = false;
				
				final boolean nearMageDoor = nrObj(1601, p);
				final boolean nrEssence    = nrObj(2491, p);
				final boolean inMageGuild  = isInMageGuild();
				
				if (!nearMageDoor && !inMageGuild) {
					log.info("Walking to mage guild.");
					walkToMageGuild();
				}
				if (nearMageDoor && !inMageGuild) {
					intrObj(1601, "Open");
					Time.sleep(1000);
				}
				if (inMageGuild) {
					intrNPC(462, "Teleport");
				}

				if (!nrEssence && !inMageGuild && inEssenceMine()) {
					Camera.setPitch(0);
					cameraSweep();
					canSeeAndWalkToNode();
				}
				break;
			case MINING_GUILD:

				final boolean nearTop = nrObj(2113, p);
				final boolean nearInnerMine = nearInnerMine();
				
				if (!nearTop  && !usedTopLadder) {
					walkToTopLadder();
					usedBtmLadder = false;
				}

				if (nearTop) {
					intrObj(2113,"Climb-down");
					usedTopLadder = true;
				}

				if (!nearInnerMine  && usedTopLadder) {
					walkToInnerMine();
				}

				if (nearInnerMine) {
					log.info("Walking to inner mine is false.");
					walkingToInnerMine = false;
				}

				break;
			case WILDERNESS_CASTLE:
				break;
			case RUNE_ROCKS:
				
				if (Widgets.get(382) != null) {
					Widgets.get(382).getChild(21).interact("Proceed");
				}
				if (!nrObjs(edgeDitchFilter, p)) {
					intrObjs(edgeDitchFilter, "Cross");
				} 

				if (nrObjs(edgeDitchFilter, p) && southOfObj(edgeDitchFilter, p)) {
					intrObjs(edgeDitchFilter, "Cross");
					Time.sleep(2000);
				}
				
				if (!southOfObj(edgeDitchFilter, p)) {
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();
				}

				break;
			default:
					Walking.newTilePath(ScriptSettings.toMinePath).traverse();
			}
		}
		
		
		public boolean southOfObj(Filter<SceneObject> obje, Tile loc) {
			SceneObject obj = SceneEntities.getNearest(obje);
			if (obj != null) {
				return obj.getLocation().getY() > loc.getY();
			}
			return false;
		}
	}

	private class RandomEventGift extends Strategy implements Task {

		@Override
		public void run() {
			if (Tabs.getCurrent() != Tabs.INVENTORY) {
				Tabs.INVENTORY.open();
			}
			
			Item i = Inventory.getItem(6183);
			if (i != null) {
				WidgetChild wc = i.getWidgetChild();
				if (wc != null) {
					wc.interact("Drop");
				}
			}
			
			Item s = Inventory.getItem(14664);
			if (s != null) {
				WidgetChild wc = s.getWidgetChild();
				if (wc != null) {
					wc.interact("Drop");
				}
			}
			
			Item[] m = Inventory.getItems(strangeRocks);
			for(int f = 0; f < m.length; f++) {
				if (m[f] != null) {
					WidgetChild zz = m[f].getWidgetChild();
					if (zz != null) {
						zz.interact("Destroy");
					}
				}
			}
			
		}
		
		@Override
		public boolean validate() {
			return Inventory.getCount(6183) > 0 || Inventory.getCount(14664) > 0 || Inventory.getCount(strangeRocks) > 0;
		}
	}
	
	private class FailSafe extends Strategy implements Task {

		Timer t = new Timer(1000);
		boolean timerSet = false;
		int FAILSTEP = 8;
		
		Timer l = new Timer(10000);
		
		boolean outOfPlaceTimerSet = false;
		
		@Override
		public void run() {
			Player p = Players.getLocal();
			if (!ScriptSettings.bankArea.contains(p) && !ScriptSettings.miningArea.contains(p.getLocation()) && Calculations.distance(ScriptSettings.miningArea.getNearest(), p.getLocation()) <= 2000) {
				if (!outOfPlaceTimerSet ) {
					l = new Timer(10000);
					outOfPlaceTimerSet = true;
					ScriptSettings.status = "Out of place.. Fixing it";
				}
				
				if (!l.isRunning() && outOfPlaceTimerSet) {
					ScriptSettings.status = "Recovering with failsafe..";
					Tile t = ScriptSettings.miningArea.getCentralTile();
					Tile f = p.getLocation();
					int y = t.getY();
					int x = t.getX();
					int nextX = 0;
					int nextY = 0;

					if (x > f.getX()) {
						nextX = getEast(FAILSTEP).getX();
					} else {
						nextX = getWest(FAILSTEP).getX();
					}

					if (y > f.getY()) {
						nextY = getNorth(FAILSTEP).getY();
					} else {
						nextY = getSouth(FAILSTEP).getY();
					}

					if (nextX != 0 && nextY != 0) {
						Walking.walk(new Tile(nextX, nextY, 0));
						log.info("Walking to : " + nextY + " " + nextY);
					}
				}
		} else {
			outOfPlaceTimerSet = false;
		}
				
			
			if (!inMiningAnimation() && ScriptSettings.miningArea.contains(p.getLocation()) 
					&& !nearDepositBox()) {
				if (!timerSet && !p.isMoving()) {
					log.info("FailSafe timer activated.");
					t = new Timer(3000);
					timerSet = true;
				}
			} else{
				timerSet = false;
			}
			
			if (!t.isRunning() && timerSet) {
				rockInteracted = false;
			}
		}
		
		@Override
		public boolean validate() {
			return guiDone && ScriptSettings.mode != Modes.ADVENTURE && !ScriptSettings.walking;
		}
	}
	
	private class Dropper extends Strategy implements Task {

		@Override
		public boolean validate() {
			if (guiDone) {
				if (Tabs.getCurrent() != Tabs.INVENTORY) {
					Tabs.INVENTORY.open();
				}
				return (Inventory.getCount() == 28)
						&& Inventory.getCount(gemFilter) != 28
						&& (ScriptSettings.miningArea.contains(Players
								.getLocal().getLocation()) || ScriptSettings.location == Locations.LRC)
						&& ((ScriptSettings.mode == Modes.POWERMINING) || (ScriptSettings.mode == Modes.BANKING && ScriptSettings.location == Locations.QUARRY));
			}
			return false;
		}

		@Override
		public void run() {
			ScriptSettings.status = "Dropping ores";
			log.info("Dropping!");
			dropAllRows();
			rockInteracted = false;
		}

	}

	@Override
	public void messageReceived(MessageEvent me) {

		String msg = me.getMessage();

		if (msg.contains("You have 30")) {
			ScriptSettings.lavaTitanSummoned = false;
		}

		if (msg.contains("You already")) {
			ScriptSettings.lavaTitanSummoned = true;
		}

		if (msg.contains("You manage to mine")) {
			ScriptSettings.oresMined++;
			ScriptSettings.totalGold += ScriptSettings.rockPrice;
		}

		if (msg.contains("You just")) {
			ScriptSettings.oresMined++;
		}
	}

	private class LavaTitanManager extends Strategy implements Task {

		@Override
		public boolean validate() {
			return guiDone && !ScriptSettings.lavaTitanSummoned
					&& hasLavaTitanPouch();
		}

		@Override
		public void run() {
			ScriptSettings.status = "Summoning Titan";
			log.info("Summoning monster!");
			drinkSummoningPotion();
			summonLavaTitan();
			ScriptSettings.lavaTitanSummoned = true;
		}

	}

	private class SuperHeatManager extends Strategy implements Task {

		@Override
		public boolean validate() {
			return guiDone && hasSuperHeatItems();
		}

		@Override
		public void run() {
			
			if (canCastSuperheat()) {
				ScriptSettings.status = "Superheating";
				castSuperHeat();
			}
		}

	}

	private class UrnManager extends Strategy implements Task {

		@Override
		public boolean validate() {
			return guiDone && hasCompleteUrns();
		}

		@Override
		public void run() {
			ScriptSettings.status = "Teleporting urns";
			teleportUrns();
		}

	}
	private class LRCDepositingManager extends Strategy implements Task {

		@Override
		public void run() {
			ScriptSettings.status = "Banking";
			log.info("Banking..");
			LRCdepositAllExceptIgnoreables();
		}

		@Override
		public boolean validate() {
			if (guiDone && ScriptSettings.location == Locations.LRC
					&& (((Inventory.getCount(ignoreableDepositItems) > 0 || LRCBankOpen())
							&& inLRCBankArea() && ScriptSettings.mode != Modes.POWERMINING))) {
				LRCDepositingState = true;
				return true;
			} else {
				LRCDepositingState = false;
				return false;
			}
		}
	}

	private class LRCPowerminer extends Strategy implements Task {

		@Override
		public void run() {
			ScriptSettings.status = "Mining";
				mineTheNearest(selectedRocks);
			}

		@Override
		public boolean validate() {
			return Inventory.getCount() < 28 && ScriptSettings.mode == Modes.BANKING && ScriptSettings.miningArea.contains(Players.getLocal().getLocation());
		}
	}

	private class SafeLRC extends Strategy implements Task {

		@Override
		public boolean validate() {
			return Players.getLocal().isInCombat() && guiDone && ScriptSettings.location == Locations.LRC;
		}

		@Override
		public void run() {
			ScriptSettings.status = "Safe Spotting";
			log.info("Safe spotting!");
			if (npcIsNorth(8832) || npcIsNorth(8833)) {
				moveSouth(5);
			}

			if (npcIsSouth(8832) || npcIsSouth(8833)) {
				moveNorth(5);
			}

			if (npcIsEast(8832) || npcIsEast(8833)) {
				moveWest(5);
			}

			if (npcIsWest(8832) || npcIsWest(8833)) {
				moveEast(5);
			}
		}
	}

	/**
	 * 
	 * Walks to LRC Bank.
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class WalkToLRCBank extends Strategy implements Task {
		@Override
		public void run() {
			if (ScriptSettings.bankArea.contains(Players.getLocal().getLocation())) {
				Walking.findPath((ScriptSettings.bankArea.getCentralTile())).traverse();
			}
		}

		@Override
		public boolean validate() {
			return Inventory.getCount() == 28
					&& ScriptSettings.mode == Modes.BANKING
					&& !ScriptSettings.bankArea.contains(Players.getLocal()
							.getLocation()) && ScriptSettings.location == Locations.LRC;
		}
	}

	/**
	 * 
	 * Keeps players safe in the wilderness
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class WildernessSafety extends Strategy implements Task {

		@Override
		public boolean validate() {
			if (guiDone && ScriptSettings.mode != Modes.POWERMINING) {
				return healthIsLow()
						|| (spiderInArea() && ScriptSettings.miningArea
								.contains(Players.getLocal().getLocation()));
			}
			return false;
		}

		@Override
		public void run() {
			ScriptSettings.status = "Being safe";
			if (healthIsLow()) {
				eatFood();
			}

			if (spiderIsAttackingWhileLogout()) {
				moveEast(8);
			}

			if (spiderIsAttacking()) {
				if (spiderIsNorth()) {
					log.info("Moving south!");
					moveSouth(2);
				}

				if (spiderIsSouth()) {
					log.info("Moving north!");
					moveNorth(2);
				}

				if (spiderIsEast()) {
					log.info("Moving east!");
					moveWest(2);
				}

				if (spiderIsWest()) {
					log.info("Moving west!");
					moveEast(2);
				}
			}
		}
	}
	
	/**
	 * 
	 * Walks players back from death.
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class DeathWalker extends Strategy implements Task {

		@Override
		public void run() {
			ScriptSettings.status = "Walking from death..";
			switch (ScriptSettings.location) {
			case WILDERNESS_CASTLE:
			case RUNE_ROCKS:
				Walking.newTilePath(ScriptSettings.deathWalkPath).traverse();
			}
		}

		@Override
		public boolean validate() {
			return haveDiedRecently();
		}
	}

	/**
	 * 
	 * Changes worlds.
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class WorldHopper extends Strategy implements Task {

		@Override
		public boolean validate() {
			
			Tile loc = Players.getLocal().getLocation();
			
			if (ScriptSettings.miningArea.contains(loc) && SceneEntities.getNearest(selectedRocks) == null && Inventory.getCount() != 28) {
				worldHopCondition = true;
			}
			
			return worldHopCondition;
			
		}

		@Override
		public void run() {
			ScriptSettings.status = "World hopping";
		}
	}

	/**
	 * 
	 * Managers what mode we are in in adventure mode.
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class AdventureSettingsManager extends Strategy implements Task {

		boolean adventurePart2Set = false;
		boolean adventurePart3Set = false;
		boolean adventurePart4Set = false;
		
		@Override
		public void run() {
			
			if (Skills.getLevel(Skills.MINING) >= 31) {
				setOurPickaxeTo(Pickaxes.ADDY_PICK);
			} else if (Skills.getLevel(Skills.MINING) >= 41) {
				setOurPickaxeTo(Pickaxes.RUNE_PICK);
			} else if (Skills.getLevel(Skills.MINING) >= 61) {
				setOurPickaxeTo(Pickaxes.RUNE_PICK);
			}
			
			if (inAdventurePart2() && !adventurePart2Set) {
				log.info("We're in adventure part 2");
				ScriptSettings.pickaxe = Pickaxes.BRONZE_PICK;
				ScriptSettings.mode = Modes.ADVENTURE;
				ScriptSettings.rock = Rocks.CLAY;

				setOurRockTo(Rocks.CLAY);
				setOurLocationTo(Locations.BARBARIAN_VILLAGE);
				
				GeItem rockP = GeItem.lookup(ScriptSettings.rockID);
				if (rockP != null) {
					ScriptSettings.rockPrice = rockP.getPrice();
				}

				adventurePart2Set = true;
			}

			if (inAdventurePart3() && !adventurePart3Set) {
				log.info("We're in Adventure part 3");
				ScriptSettings.pickaxe = Pickaxes.BRONZE_PICK;
				setOurRockTo(Rocks.CLAY);
				setOurRock2To(Rocks.TIN);
				setOurLocationTo(Locations.BARBARIAN_VILLAGE);
				ScriptSettings.mode = Modes.BANKING;
				adventurePart3Set = true;

				GeItem rockP = GeItem.lookup(ScriptSettings.rockID);
				if (rockP != null) {
					ScriptSettings.rockPrice = rockP.getPrice();
				}

				adventurePart3Set = true;
			}
			
			if (inAdventurePart4() && !adventurePart4Set) {
				log.info("We're in adventure part 4");
				ScriptSettings.pickaxe = Pickaxes.MITH_PICK;
				setOurRockTo(Rocks.IRON);
				setOurRock2To(Rocks.IRON);
				setOurLocationTo(Locations.RIMMINGTON);
				ScriptSettings.mode = Modes.ADVENTURE;
				adventurePart4Set = true;
			}
			
			if (inAdventurePart6()) {
				log.info("We're in adventure part 6");
				ScriptSettings.mode = Modes.ADVENTURE;
			}
			
			if (inAdventurePart7()) {
				log.info("We're in adventure part 7");
				buyPickaxes();
			}
		}

		@Override
		public boolean validate() {
			return true;
		}
	}

	/**
	 * 
	 * Controls walking from place to place in adventure mode
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class AdventurePathing extends Strategy implements Task {

		public void walkToSouthBarb() {
			Walking.newTilePath(walkToSouthBarb).traverse();
		}
		
		@Override
		public void run() {

			if (inAdventurePart2()) {
				ScriptSettings.status = "Walking to Varrock West!";
				walkToSouthBarb();
			}

			if (inAdventurePart5()) {
				ScriptSettings.mode = Modes.ADVENTURE;
				ScriptSettings.status = "Walking to GE!";
				walkToGE();
			}
			
			if (inAdventurePart6()) {
				sellClay();
			}
		}

		@Override
		public boolean validate() {
			return true;
		}
	}

	/**
	 * 
	 * Primitive antiban
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class AntiBan extends Strategy implements Task {
		@Override
		public boolean validate() {
			return (guiDone && ScriptSettings.antiBanOn);
		}

		@Override
		public void run() {
			ScriptSettings.status = "AntiBan";
			switch (Random.nextInt(0, 1000)) {

			case 1:
				log.info("KingBan Activated.");
				int randX = Random.nextInt(0, 25000);
				int randY = Random.nextInt(0, 25000);
				Camera.turnTo(new Tile(randX, randY, 0));
				break;
			case 2:
				log.info("KingBan Activated.");
				Tabs.FRIENDS.open();
				break;
			case 3:
				log.info("KingBan Activated.");
				Tabs.CLAN_CHAT.open();
				break;
			case 4:
				log.info("KingBan Activated.");
				Tabs.MAGIC.open();
				Tabs.EQUIPMENT.open();
				break;
			case 5:
				log.info("Antiban Activated.");
				Tabs.INVENTORY.open();
				break;
			case 6:
				log.info("KingBan Activated.");
				int randA = Random.nextInt(0, 25000);
				Camera.setAngle(randA);
				Camera.setPitch(randA);
				break;
			case 8:
				log.info("KingBan Activated.");
				Tabs.EMOTES.open();
				break;
			}

		}
	}

	/**
	 * 
	 * Use this when powermining.
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class PowermineDrop extends Strategy implements Task {

		@Override
		public boolean validate() {
			if (guiDone) {
				return (ScriptSettings.miningArea.contains(Players.getLocal()
						.getLocation())
						&& ScriptSettings.mode == Modes.POWERMINING
						&& SceneEntities.getLoaded(selectedRocks) != null && Inventory
							.getCount() != 28);
			}
			return false;
		}

		@Override
		public void run() {
			ScriptSettings.status = "Powermining";
				log.info("Powermining");
				mineTheNearest(selectedRocks);
				log.info("Cant mine nearest..");
		}

	}

	/**
	 * 
	 * Moves the paint
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class MoveBox extends Strategy implements Task {

		@Override
		public boolean validate() {
			return moveBox;
		}

		@Override
		public void run() {
			log.info("Moving box..");
			PAINT_X = Mouse.getX() - 495;
			PAINT_Y = Mouse.getY() - 20;
		}
	}

	public boolean mouseInMoveArea(int x, int y) {
		return x > MOVEBOX_X && x < MOVEBOX_X + 20 && y > MOVEBOX_Y
				&& y < MOVEBOX_Y + (RECT_HEIGHT / 2);
	}

	public boolean mouseInHideArea(int x, int y) {
		return x > HIDEBOX_X && x < HIDEBOX_X + 20
				&& y > HIDEBOX_Y + (RECT_HEIGHT / 2)
				&& y < HIDEBOX_Y + (RECT_HEIGHT);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point mouseP = arg0.getPoint();
		int x = (int) mouseP.getX();
		int y = (int) mouseP.getY();
		if (mouseInMoveArea(x, y)) {
			log.info("In move area");
			if (!moveBox) {
				log.info("Moving box");
				moveBox = true;
			}
		}
		if (mouseInHideArea(x, y)) {
			log.info("In hide area");
			if (hideBox) {
				hideBox = false;

			} else if (!hideBox) {
				hideBox = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		moveBox = false;
	}

}

final class GeItem {

	public static final String HOST = "http://services.runescape.com";
	public static final String GET_ID = "/m=itemdb_rs/api/catalogue/detail.json?item=";
	public static final String GET_NAME = "/m=itemdb_rs/results.ws?query=";

	private int id;
	private int price;
	private int changeToday;
	private double[] changes;
	private boolean members;
	private String name;
	private String description;
	private String iconUrl;
	private String largeIconUrl;
	private String type;
	private String typeIconUrl;

	private GeItem(int id, String name, int price, boolean members,
			int changeToday, double[] changes, String description,
			String iconUrl, String largeIconUrl, String type, String typeIconUrl) {
		this.id = id;
		this.price = price;
		this.changeToday = changeToday;
		this.changes = changes;
		this.members = members;
		this.name = name;
		this.description = description;
		this.iconUrl = iconUrl;
		this.largeIconUrl = largeIconUrl;
		this.type = type;
		this.typeIconUrl = typeIconUrl;
	}

	public double getChange30Days() {
		return changes[0];
	}

	public double getChange90Days() {
		return changes[1];
	}

	public double getChange180Days() {
		return changes[2];
	}

	public int getChangeToday() {
		return changeToday;
	}

	public String getDescription() {
		return description;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public int getId() {
		return id;
	}

	public String getLargeIconUrl() {
		return largeIconUrl;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}

	public String getTypeIconUrl() {
		return typeIconUrl;
	}

	public boolean isMembers() {
		return members;
	}

	public static GeItem lookup(int itemId) {
		try {
			URL url = new URL(HOST + GET_ID + itemId);
			URLConnection con = url.openConnection();
			con.setReadTimeout(10000);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder jsonsb = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null) {
				jsonsb.append(line);
			}
			String json = jsonsb.toString();
			String name = searchJSON(json, "item", "name");
			int price = parseMultiplier(searchJSON(json, "item", "current",
					"price"));
			boolean members = Boolean.parseBoolean(searchJSON(json, "item",
					"members"));
			int changeToday = parseMultiplier(searchJSON(json, "item", "today",
					"price"));
			double change30 = Double.parseDouble(searchJSON(json, "item",
					"day30", "change").replace("%", ""));
			double change90 = Double.parseDouble(searchJSON(json, "item",
					"day90", "change").replace("%", ""));
			double change180 = Double.parseDouble(searchJSON(json, "item",
					"day180", "change").replace("%", ""));
			double[] changes = { change30, change90, change180 };
			String description = searchJSON(json, "item", "description");
			String iconUrl = searchJSON(json, "item", "icon");
			String largeIconUrl = searchJSON(json, "item", "icon_large");
			String type = searchJSON(json, "item", "type");
			String typeIconUrl = searchJSON(json, "item", "typeIcon");
			return new GeItem(itemId, name, price, members, changeToday,
					changes, description, iconUrl, largeIconUrl, type,
					typeIconUrl);
		} catch (Exception e) {
		}
		return null;
	}

	public static GeItem lookup(String itemName) {
		try {
			itemName = itemName.toLowerCase();
			URL url = new URL(HOST + GET_NAME + itemName.replaceAll(" ", "+"));
			URLConnection con = url.openConnection();
			con.setReadTimeout(10000);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder srcsb = new StringBuilder();
			String line = null;
			while ((line = in.readLine()) != null) {
				srcsb.append(line);
			}
			String src = srcsb.substring(
					srcsb.indexOf("<table class=\"results\">"),
					srcsb.indexOf("<p id=\"res-tips\">"));
			Pattern p = Pattern.compile(".*?/" + itemName.replaceAll(" ", "_")
					+ "/viewitem\\.ws\\?obj=([\\d]+?)\\\">" + itemName
					+ "</a>.*");
			Matcher m = p.matcher(src.toLowerCase());
			if (m.find()) {
				int id = Integer.parseInt(m.group(1));
				return lookup(id);
			}
		} catch (Exception e) {
		}
		return null;
	}

	private static int parseMultiplier(String str) {
		if (str.matches("-?\\d+(\\.\\d+)?[kmb]")) {
			return (int) (Double
					.parseDouble(str.substring(0, str.length() - 1)) * (str
					.endsWith("b") ? 1000000000D : str.endsWith("m") ? 1000000
					: str.endsWith("k") ? 1000 : 1));
		} else {
			return Integer.parseInt(str);
		}
	}

	private static String searchJSON(String json, String... keys) {
		String search = "\"" + keys[0] + "\":";
		int idx = json.indexOf(search) + search.length();
		if (keys.length > 1 && json.charAt(idx) == '{') {
			String[] subKeys = new String[keys.length - 1];
			System.arraycopy(keys, 1, subKeys, 0, subKeys.length);
			return searchJSON(json.substring(idx), subKeys);
		}
		Pattern p = Pattern.compile(".*?[,\\{]\\\"" + keys[0]
				+ "\\\":(-?[\\d]|[\\\"\\d].*?[kmb]?[^\\\\][\\\"\\d])[,\\}].*");
		Matcher m = p.matcher(json);
		if (m.find()) {
			String value = m.group(1);
			if (value.matches("\\\".*?\\\"")) {
				value = value.substring(1, value.length() - 1);
			}
			return value;
		}
		return "";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append("[");
		java.lang.reflect.Method[] methods = GeItem.class.getMethods();
		for (int i = 0; i < methods.length; i++) {
			java.lang.reflect.Method method = methods[i];
			Package pack = method.getDeclaringClass().getPackage();
			if (pack == null || pack.equals(GeItem.class.getPackage())) {
				if ((method.getParameterTypes().length | method
						.getAnnotations().length) != 0) {
					continue;
				}
				String methodName = method.getName();
				if (methodName.equals("getName")
						|| methodName.equals("toString")) {
					continue;
				}
				sb.append(methodName).append("=");
				try {
					sb.append(method.invoke(this, new Object[0]));
				} catch (Exception ignored) {
				}
				sb.append(",");
			}
		}
		String string = sb.toString();
		return string.substring(0, string.lastIndexOf(",")) + "]";
	}

}
