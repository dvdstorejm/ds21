package ds2driver.jmeter.samplers;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;

import ds2driver.jmeter.beans.GlobalConstants;
import ds2driver.jmeter.beans.User;

public class BrowseTypeCalculatorSampler extends AbstractJavaSamplerClient {

	Logger logger = getNewLogger();
	User user;
	String actor_in;
	String title_in;
	String browse_type_in;
	String browse_category_in;
	String browse_actor_in;
	String browse_title_in;
	String browse_criteria;
	
	public SampleResult runTest(JavaSamplerContext jsc) {
		
		SampleResult result = new SampleResult();
        JMeterVariables vars = JMeterContextService.getContext().getVariables();
        
        result.sampleStart(); // start stopwatch

        user = (User)vars.getObject(GlobalConstants.USER);
        
        if (user == null) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setSampleLabel("FAILURE: BrowseTypeCalculatorSampler usr null");
        } else {
        	
        	// Search Product table different ways:
            // Browse by Category: with category randomized between 1 and MAX_CATEGORY (and SPECIAL=1)
            // Browse by Actor:  with first and last names selected randomly from list of names
            // Browse by Title:  with first and last words in title selected randomly from list of title words
        	
        	int batch_size_in = 1 + user.nextInt ( 2 * user.getSearch_batch_size() - 1 ); // request avg of search_batch_size lines
            int search_type = user.nextInt( 3 ); // randomly select search type

            switch ( search_type )
            {
            case 0:  // Search by Category
              browse_type_in = "category";
              browse_category_in = Integer.toString(1 + user.nextInt( GlobalConstants.MAX_CATEGORY ));
              browse_actor_in = "";
              browse_title_in = "";
              browse_criteria = browse_category_in;
              break;
            case 1:  // Search by Actor 
              browse_type_in = "actor";
              browse_category_in = "";
              CreateActor ( );
              browse_actor_in = actor_in;
              browse_title_in = "";
              browse_criteria = browse_actor_in;
              break;
            case 2:  // Search by Title
              browse_type_in = "title";
              browse_category_in = "";
              browse_actor_in = "";
              CreateTitle ( );
              browse_title_in = title_in;
              browse_criteria = browse_title_in;
              break;
            }

            vars.put("search_type", Integer.toString(search_type));

            vars.put("batch_size_in", Integer.toString(batch_size_in));
            vars.put("browse_type_in", browse_type_in);
            vars.put("browse_category_in", browse_category_in);
            vars.put("browse_actor_in", browse_actor_in);
            vars.put("browse_title_in", browse_title_in);
            vars.put("browse_criteria", browse_criteria);

    		logger.debug("BrowseTypeCalculatorSampler: batch_size_in " + batch_size_in);
    		logger.debug("BrowseTypeCalculatorSampler: search_type " + search_type);
            
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(true);
            result.setSampleLabel("SUCCESS: BrowseTypeCalculatorSampler");
        }
        
        return result;
	}
	
	 void CreateActor ( )
     {
	    // Names compiled by Dara Jaffe
	
		// 200 actor/actress firstnames
		String[] actor_firstnames = new String[]
		{
		"ADAM", "ADRIEN", "AL", "ALAN", "ALBERT", "ALEC", "ALICIA", "ANDY", "ANGELA", "ANGELINA", "ANJELICA", 
		"ANNE", "ANNETTE", "ANTHONY", "AUDREY", "BELA", "BEN", "BETTE", "BOB", "BRAD", "BRUCE", "BURT", "CAMERON", 
		"CANDICE", "CARMEN", "CARRIE", "CARY", "CATE", "CHARLES", "CHARLIZE", "CHARLTON", "CHEVY", "CHRIS", 
		"CHRISTIAN", "CHRISTOPHER", "CLARK", "CLINT", "CUBA", "DAN", "DANIEL", "DARYL", "DEBBIE", "DENNIS", 
		"DENZEL", "DIANE", "DORIS", "DREW", "DUSTIN", "ED", "EDWARD", "ELIZABETH", "ELLEN", "ELVIS", "EMILY", 
		"ETHAN", "EWAN", "FARRAH", "FAY", "FRANCES", "FRANK", "FRED", "GARY", "GENE", "GEOFFREY", "GINA", "GLENN", 
		"GOLDIE", "GRACE", "GREG", "GREGORY", "GRETA", "GROUCHO", "GWYNETH", "HALLE", "HARRISON", "HARVEY", 
		"HELEN", "HENRY", "HILARY", "HUGH", "HUME", "HUMPHREY", "IAN", "INGRID", "JACK", "JADA", "JAMES", "JANE", 
		"JAYNE", "JEFF", "JENNIFER", "JEREMY", "JESSICA", "JIM", "JOAN", "JODIE", "JOE", "JOHN", "JOHNNY", "JON", 
		"JUDE", "JUDI", "JUDY", "JULIA", "JULIANNE", "JULIETTE", "KARL", "KATE", "KATHARINE", "KENNETH", "KEVIN", 
		"KIM", "KIRK", "KIRSTEN", "LANA", "LAURA", "LAUREN", "LAURENCE", "LEELEE", "LENA", "LEONARDO", "LIAM", 
		"LISA", "LIV", "LIZA", "LUCILLE", "MADELINE", "MAE", "MARILYN", "MARISA", "MARLENE", "MARLON", "MARY", 
		"MATT", "MATTHEW", "MEG", "MEL", "MENA", "MERYL", "MICHAEL", "MICHELLE", "MILLA", "MINNIE", "MIRA", 
		"MORGAN", "NATALIE", "NEVE", "NICK", "NICOLAS", "NICOLE", "OLYMPIA", "OPRAH", "ORLANDO", "PARKER", 
		"PAUL", "PEARL", "PENELOPE", "RALPH", "RAY", "REESE", "RENEE", "RICHARD", "RIP", "RITA", "RIVER", 
		"ROBERT", "ROBIN", "ROCK", "ROSIE", "RUBY", "RUSSELL", "SALLY", "SALMA", "SANDRA", "SCARLETT", "SEAN", 
		"SHIRLEY", "SIDNEY", "SIGOURNEY", "SISSY", "SOPHIA", "SPENCER", "STEVE", "SUSAN", "SYLVESTER", "THORA", 
		"TIM", "TOM", "UMA", "VAL", "VIVIEN", "WALTER", "WARREN", "WHOOPI", "WILL", "WILLEM", "WILLIAM", "WINONA", 
		"WOODY", "ZERO"
		};

		// 200 actor/actress lastnames  
		String[] actor_lastnames = new String[]
		{
		"AFFLECK", "AKROYD", "ALLEN", "ANISTON", "ASTAIRE", "BACALL", "BAILEY", "BALE", "BALL", "BARRYMORE", 
		"BASINGER", "BEATTY", "BENING", "BERGEN", "BERGMAN", "BERRY", "BIRCH", "BLANCHETT", "BLOOM", "BOGART", 
		"BOLGER", "BRANAGH", "BRANDO", "BRIDGES", "BRODY", "BULLOCK", "CAGE", "CAINE", "CAMPBELL", "CARREY", 
		"CHAPLIN", "CHASE", "CLOSE", "COOPER", "COSTNER", "CRAWFORD", "CRONYN", "CROWE", "CRUISE", "CRUZ", 
		"DAFOE", "DAMON", "DAVIS", "DAY", "DAY-LEWIS", "DEAN", "DEE", "DEGENERES", "DENCH", "DENIRO", 
		"DEPP", "DERN", "DIAZ", "DICAPRIO", "DIETRICH", "DOUGLAS", "DREYFUSS", "DRIVER", "DUKAKIS", "DUNST", 
		"EASTWOOD", "FAWCETT", "FIELD", "FIENNES", "FINNEY", "FISHER", "FONDA", "FORD", "FOSTER", "FREEMAN", 
		"GABLE", "GARBO", "GARCIA", "GARLAND", "GIBSON", "GOLDBERG", "GOODING", "GRANT", "GUINESS", "HACKMAN", 
		"HANNAH", "HARRIS", "HAWKE", "HAWN", "HAYEK", "HECHE", "HEPBURN", "HESTON", "HOFFMAN", "HOPE", 
		"HOPKINS", "HOPPER", "HORNE", "HUDSON", "HUNT", "HURT", "HUSTON", "IRONS", "JACKMAN", "JOHANSSON", 
		"JOLIE", "JOVOVICH", "KAHN", "KEATON", "KEITEL", "KELLY", "KIDMAN", "KILMER", "KINNEAR", "KUDROW", 
		"LANCASTER", "LANSBURY", "LAW", "LEIGH", "LEWIS", "LOLLOBRIGIDA", "LOREN", "LUGOSI", "MALDEN", "MANSFIELD", 
		"MARTIN", "MARX", "MATTHAU", "MCCONAUGHEY", "MCDORMAND", "MCGREGOR", "MCKELLEN", "MCQUEEN", "MINELLI", "MIRANDA",  
		"MONROE", "MOORE", "MOSTEL", "NEESON", "NEWMAN", "NICHOLSON", "NOLTE", "NORTON", "DONNELL", "OLIVIER", 
		"PACINO", "PALTROW", "PECK", "PENN", "PESCI", "PFEIFFER", "PHOENIX", "PINKETT", "PITT", "POITIER", 
		"POSEY", "PRESLEY", "REYNOLDS", "RICKMAN", "ROBBINS", "ROBERTS", "RUSH", "RUSSELL", "RYAN", "RYDER", 
		"SANDLER", "SARANDON", "SILVERSTONE", "SINATRA", "SMITH", "SOBIESKI", "SORVINO", "SPACEK", "STALLONE", "STREEP", 
		"SUVARI", "SWANK", "TANDY", "TAUTOU", "TAYLOR", "TEMPLE", "THERON", "THURMAN", "TOMEI", "TORN", 
		"TRACY", "TURNER", "TYLER", "VOIGHT", "WAHLBERG", "WALKEN", "WASHINGTON", "WATSON", "WAYNE", "WEAVER", 
		"WEST", "WILLIAMS", "WILLIS", "WILSON", "WINFREY", "WINSLET", "WITHERSPOON", "WOOD", "WRAY", "ZELLWEGER"
		};

		actor_in = actor_firstnames[user.nextInt( 200 )] + " " + actor_lastnames[user.nextInt( 200 )];

     }  // End of CreateActor

	 void CreateTitle ( )
     {
		// Names compiled by Dara Jaffe
		
		// 1000 movie title words
		 String[] movie_titles = new String[]
		 {
		 "ACADEMY", "ACE", "ADAPTATION", "AFFAIR", "AFRICAN", "AGENT", "AIRPLANE", "AIRPORT", "ALABAMA", "ALADDIN", 
		 "ALAMO", "ALASKA", "ALI", "ALICE", "ALIEN", "ALLEY", "ALONE", "ALTER", "AMADEUS", "AMELIE", 
		 "AMERICAN", "AMISTAD", "ANACONDA", "ANALYZE", "ANGELS", "ANNIE", "ANONYMOUS", "ANTHEM", "ANTITRUST", "ANYTHING", 
		 "APACHE", "APOCALYPSE", "APOLLO", "ARABIA", "ARACHNOPHOBIA", "ARGONAUTS", "ARIZONA", "ARK", "ARMAGEDDON", "ARMY", 
		 "ARSENIC", "ARTIST", "ATLANTIS", "ATTACKS", "ATTRACTION", "AUTUMN", "BABY", "BACKLASH", "BADMAN", "BAKED", 
		 "BALLOON", "BALLROOM", "BANG", "BANGER", "BARBARELLA", "BAREFOOT", "BASIC", "BEACH", "BEAR", "BEAST", 
		 "BEAUTY", "BED", "BEDAZZLED", "BEETHOVEN", "BEHAVIOR", "BENEATH", "BERETS", "BETRAYED", "BEVERLY", "BIKINI", 
		 "BILKO", "BILL", "BINGO", "BIRCH", "BIRD", "BIRDCAGE", "BIRDS", "BLACKOUT", "BLADE", "BLANKET", 
		 "BLINDNESS", "BLOOD", "BLUES", "BOILED", "BONNIE", "BOOGIE", "BOONDOCK", "BORN", "BORROWERS", "BOULEVARD", 
		 "BOUND", "BOWFINGER", "BRANNIGAN", "BRAVEHEART", "BREAKFAST", "BREAKING", "BRIDE", "BRIGHT", "BRINGING", "BROOKLYN", 
		 "BROTHERHOOD", "BUBBLE", "BUCKET", "BUGSY", "BULL", "BULWORTH", "BUNCH", "BUTCH", "BUTTERFLY", "CABIN", 
		 "CADDYSHACK", "CALENDAR", "CALIFORNIA", "CAMELOT", "CAMPUS", "CANDIDATE", "CANDLES", "CANYON", "CAPER", "CARIBBEAN", 
		 "CAROL", "CARRIE", "CASABLANCA", "CASPER", "CASSIDY", "CASUALTIES", "CAT", "CATCH", "CAUSE", "CELEBRITY", 
		 "CENTER", "CHAINSAW", "CHAMBER", "CHAMPION", "CHANCE", "CHAPLIN", "CHARADE", "CHARIOTS", "CHASING", "CHEAPER", 
		 "CHICAGO", "CHICKEN", "CHILL", "CHINATOWN", "CHISUM", "CHITTY", "CHOCOLAT", "CHOCOLATE", "CHRISTMAS", "CIDER", 
		 "CINCINATTI", "CIRCUS", "CITIZEN", "CLASH", "CLEOPATRA", "CLERKS", "CLOCKWORK", "CLONES", "CLOSER", "CLUB", 
		 "CLUE", "CLUELESS", "CLYDE", "COAST", "COLDBLOODED", "COLOR", "COMA", "COMANCHEROS", "COMFORTS", "COMMAND", 
		 "COMMANDMENTS", "CONEHEADS", "CONFESSIONS", "CONFIDENTIAL", "CONFUSED", "CONGENIALITY", "CONNECTICUT", "CONNECTION", 
		 "CONQUERER", "CONSPIRACY", "CONTACT", "CONTROL", "CONVERSATION", "CORE", "COWBOY", "CRAFT", "CRANES", "CRAZY", 
		 "CREATURES", "CREEPERS", "CROOKED", "CROSSING", "CROSSROADS", "CROW", "CROWDS", "CRUELTY", "CRUSADE", "CRYSTAL", 
		 "CUPBOARD", "CURTAIN", "CYCLONE", "DADDY", "DAISY", "DALMATIONS", "DANCES", "DANCING", "DANGEROUS", "DARES", 
		 "DARKNESS", "DARKO", "DARLING", "DARN", "DATE", "DAUGHTER", "DAWN", "DAY", "DAZED", "DECEIVER", "DEEP", "DEER", 
		 "DELIVERANCE", "DESERT", "DESIRE", "DESPERATE", "DESTINATION", "DESTINY", "DETAILS", "DETECTIVE", "DEVIL", "DIARY", 
		 "DINOSAUR", "DIRTY", "DISCIPLE", "DISTURBING", "DIVIDE", "DIVINE", "DIVORCE", "DOCTOR", "DOGMA", "DOLLS", 
		 "DONNIE", "DOOM", "DOORS", "DORADO", "DOUBLE", "DOUBTFIRE", "DOWNHILL", "DOZEN", "DRACULA", "DRAGON", 
		 "DRAGONFLY", "DREAM", "DRIFTER", "DRIVER", "DRIVING", "DROP", "DRUMLINE", "DRUMS", "DUCK", "DUDE", 
		 "DUFFEL", "DUMBO", "DURHAM", "DWARFS", "DYING", "DYNAMITE", "EAGLES", "EARLY", "EARRING", "EARTH", 
		 "EASY", "EDGE", "EFFECT", "EGG", "EGYPT", "ELEMENT", "ELEPHANT", "ELF", "ELIZABETH", "EMPIRE", 
		 "ENCINO", "ENCOUNTERS", "ENDING", "ENEMY", "ENGLISH", "ENOUGH", "ENTRAPMENT", "ESCAPE", "EVE", "EVERYONE", "EVOLUTION", 
		 "EXCITEMENT", "EXORCIST", "EXPECATIONS", "EXPENDABLE", "EXPRESS", "EXTRAORDINARY", "EYES", "FACTORY", "FALCON", 
		 "FAMILY", "FANTASIA", "FANTASY", "FARGO", "FATAL", "FEATHERS", "FELLOWSHIP", "FERRIS", "FEUD", "FEVER", 
		 "FICTION", "FIDDLER", "FIDELITY", "FIGHT", "FINDING", "FIRE", "FIREBALL", "FIREHOUSE", "FISH", "FLAMINGOS", 
		 "FLASH", "FLATLINERS", "FLIGHT", "FLINTSTONES", "FLOATS", "FLYING", "FOOL", "FOREVER", "FORREST", "FORRESTER", 
		 "FORWARD", "FRANKENSTEIN", "FREAKY", "FREDDY", "FREEDOM", "FRENCH", "FRIDA", "FRISCO", "FROGMEN", "FRONTIER", 
		 "FROST", "FUGITIVE", "FULL", "FURY", "GABLES", "GALAXY", "GAMES", "GANDHI", "GANGS", "GARDEN", 
		 "GASLIGHT", "GATHERING", "GENTLEMEN", "GHOST", "GHOSTBUSTERS", "GIANT", "GILBERT", "GILMORE", "GLADIATOR", "GLASS", 
		 "GLEAMING", "GLORY", "GO", "GODFATHER", "GOLD", "GOLDFINGER", "GOLDMINE", "GONE", "GOODFELLAS", "GORGEOUS", 
		 "GOSFORD", "GRACELAND", "GRADUATE", "GRAFFITI", "GRAIL", "GRAPES", "GREASE", "GREATEST", "GREEDY", "GREEK", 
		 "GRINCH", "GRIT", "GROOVE", "GROSSE", "GROUNDHOG", "GUMP", "GUN", "GUNFIGHT", "GUNFIGHTER", "GUYS", 
		 "HALF", "HALL", "HALLOWEEN", "HAMLET", "HANDICAP", "HANGING", "HANKY", "HANOVER", "HAPPINESS", "HARDLY", 
		 "HAROLD", "HARPER", "HARRY", "HATE", "HAUNTED", "HAUNTING", "HAWK", "HEAD", "HEARTBREAKERS", "HEAVEN", 
		 "HEAVENLY", "HEAVYWEIGHTS", "HEDWIG", "HELLFIGHTERS", "HIGH", "HIGHBALL", "HILLS", "HOBBIT", "HOCUS", "HOLES", 
		 "HOLIDAY", "HOLLOW", "HOLLYWOOD", "HOLOCAUST", "HOLY", "HOME", "HOMEWARD", "HOMICIDE", "HONEY", "HOOK", 
		 "HOOSIERS", "HOPE", "HORN", "HORROR", "HOTEL", "HOURS", "HOUSE", "HUMAN", "HUNCHBACK", "HUNGER", 
		 "HUNTER", "HUNTING", "HURRICANE", "HUSTLER", "HYDE", "HYSTERICAL", "ICE", "IDAHO", "IDENTITY", "IDOLS", 
		 "IGBY", "ILLUSION", "IMAGE", "IMPACT", "IMPOSSIBLE", "INCH", "INDEPENDENCE", "INDIAN", "INFORMER", "INNOCENT", 
		 "INSECTS", "INSIDER", "INSTINCT", "INTENTIONS", "INTERVIEW", "INTOLERABLE", "INTRIGUE", "INVASION", "IRON", "ISHTAR", 
		 "ISLAND", "ITALIAN", "JACKET", "JADE", "JAPANESE", "JASON", "JAWBREAKER", "JAWS", "JEDI", "JEEPERS", 
		 "JEKYLL", "JEOPARDY", "JERICHO", "JERK", "JERSEY", "JET", "JINGLE", "JOON", "JUGGLER", "JUMANJI", 
		 "JUMPING", "JUNGLE", "KANE", "KARATE", "KENTUCKIAN", "KICK", "KILL", "KILLER", "KING", "KISS", 
		 "KISSING", "KNOCK", "KRAMER", "KWAI", "LABYRINTH", "LADY", "LADYBUGS", "LAMBS", "LANGUAGE", "LAWLESS", 
		 "LAWRENCE", "LEAGUE", "LEATHERNECKS", "LEBOWSKI", "LEGALLY", "LEGEND", "LESSON", "LIAISONS", "LIBERTY", "LICENSE", 
		 "LIES", "LIFE", "LIGHTS", "LION", "LOATHING", "LOCK", "LOLA", "LOLITA", "LONELY", "LORD", 
		 "LOSE", "LOSER", "LOST", "LOUISIANA", "LOVE", "LOVELY", "LOVER", "LOVERBOY", "LUCK", "LUCKY", 
		 "LUKE", "LUST", "MADIGAN", "MADISON", "MADNESS", "MADRE", "MAGIC", "MAGNIFICENT", "MAGNOLIA", "MAGUIRE", 
		 "MAIDEN", "MAJESTIC", "MAKER", "MALKOVICH", "MALLRATS", "MALTESE", "MANCHURIAN", "MANNEQUIN", "MARRIED", "MARS", 
		 "MASK", "MASKED", "MASSACRE", "MASSAGE", "MATRIX", "MAUDE", "MEET", "MEMENTO", "MENAGERIE", "MERMAID", 
		 "METAL", "METROPOLIS", "MICROCOSMOS", "MIDNIGHT", "MIDSUMMER", "MIGHTY", "MILE", "MILLION", "MINDS", "MINE", 
		 "MINORITY", "MIRACLE", "MISSION", "MIXED", "MOB", "MOCKINGBIRD", "MOD", "MODEL", "MODERN", "MONEY", 
		 "MONSOON", "MONSTER", "MONTEREY", "MONTEZUMA", "MOON", "MOONSHINE", "MOONWALKER", "MOSQUITO", "MOTHER", "MOTIONS", 
		 "MOULIN", "MOURNING", "MOVIE", "MULAN", "MULHOLLAND", "MUMMY", "MUPPET", "MURDER", "MUSCLE", "MUSIC", 
		 "MUSKETEERS", "MUSSOLINI", "MYSTIC", "NAME", "NASH", "NATIONAL", "NATURAL", "NECKLACE", "NEIGHBORS", "NEMO", 
		 "NETWORK", "NEWSIES", "NEWTON", "NIGHTMARE", "NONE", "NOON", "NORTH", "NORTHWEST", "NOTORIOUS", "NOTTING", 
		 "NOVOCAINE", "NUTS", "OCTOBER", "ODDS", "OKLAHOMA", "OLEANDER", "OPEN", "OPERATION", "OPPOSITE", "OPUS", 
		 "ORANGE", "ORDER", "ORIENT", "OSCAR", "OTHERS", "OUTBREAK", "OUTFIELD", "OUTLAW", "OZ", "PACIFIC", 
		 "PACKER", "PAJAMA", "PANIC", "PANKY", "PANTHER", "PAPI", "PARADISE", "PARIS", "PARK", "PARTY", 
		 "PAST", "PATHS", "PATIENT", "PATRIOT", "PATTON", "PAYCHECK", "PEACH", "PEAK", "PEARL", "PELICAN", 
		 "PERDITION", "PERFECT", "PERSONAL", "PET", "PHANTOM", "PHILADELPHIA", "PIANIST", "PICKUP", "PILOT", "PINOCCHIO", 
		 "PIRATES", "PITTSBURGH", "PITY", "PIZZA", "PLATOON", "PLUTO", "POCUS", "POLISH", "POLLOCK", "POND", 
		 "POSEIDON", "POTLUCK", "POTTER", "PREJUDICE", "PRESIDENT", "PRIDE", "PRIMARY", "PRINCESS", "PRIVATE", "PRIX", 
		 "PSYCHO", "PULP", "PUNK", "PURE", "PURPLE", "QUEEN", "QUEST", "QUILLS", "RACER", "RAGE", 
		 "RAGING", "RAIDERS", "RAINBOW", "RANDOM", "RANGE", "REAP", "REAR", "REBEL", "RECORDS", "REDEMPTION", 
		 "REDS", "REEF", "REIGN", "REMEMBER", "REQUIEM", "RESERVOIR", "RESURRECTION", "REUNION", "RIDER", "RIDGEMONT", 
		 "RIGHT", "RINGS", "RIVER", "ROAD", "ROBBERS", "ROBBERY", "ROCK", "ROCKETEER", "ROCKY", "ROLLERCOASTER", 
		 "ROMAN", "ROOF", "ROOM", "ROOTS", "ROSES", "ROUGE", "ROXANNE", "RUGRATS", "RULES", "RUN", 
		 "RUNAWAY", "RUNNER", "RUSH", "RUSHMORE", "SABRINA", "SADDLE", "SAGEBRUSH", "SAINTS", "SALUTE", "SAMURAI", 
		 "SANTA", "SASSY", "SATISFACTION", "SATURDAY", "SATURN", "SAVANNAH", "SCALAWAG", "SCARFACE", "SCHOOL", "SCISSORHANDS", 
		 "SCORPION", "SEA", "SEABISCUIT", "SEARCHERS", "SEATTLE", "SECRET", "SECRETARY", "SECRETS", "SENSE", "SENSIBILITY", 
		 "SEVEN", "SHAKESPEARE", "SHANE", "SHANGHAI", "SHAWSHANK", "SHEPHERD", "SHINING", "SHIP", "SHOCK", "SHOOTIST", 
		 "SHOW", "SHREK", "SHRUNK", "SIDE", "SIEGE", "SIERRA", "SILENCE", "SILVERADO", "SIMON", "SINNERS", 
		 "SISTER", "SKY", "SLACKER", "SLEEPING", "SLEEPLESS", "SLEEPY", "SLEUTH", "SLING", "SLIPPER", "SLUMS", 
		 "SMILE", "SMOKING", "SMOOCHY", "SNATCH", "SNATCHERS", "SNOWMAN", "SOLDIERS", "SOMETHING", "SONG", "SONS", 
		 "SORORITY", "SOUP", "SOUTH", "SPARTACUS", "SPEAKEASY", "SPEED", "SPICE", "SPIKING", "SPINAL", "SPIRIT", 
		 "SPIRITED", "SPLASH", "SPLENDOR", "SPOILERS", "SPY", "SQUAD", "STAGE", "STAGECOACH", "STALLION", "STAMPEDE", 
		 "STAR", "STATE", "STEEL", "STEERS", "STEPMOM", "STING", "STOCK", "STONE", "STORM", "STORY", 
		 "STRAIGHT", "STRANGELOVE", "STRANGER", "STRANGERS", "STREAK", "STREETCAR", "STRICTLY", "SUBMARINE", "SUGAR", "SUICIDES", 
		 "SUIT", "SUMMER", "SUN", "SUNDANCE", "SUNRISE", "SUNSET", "SUPER", "SUPERFLY", "SUSPECTS", "SWARM", 
		 "SWEDEN", "SWEET", "SWEETHEARTS", "TADPOLE", "TALENTED", "TARZAN", "TAXI", "TEEN", "TELEGRAPH", "TELEMARK", 
		 "TEMPLE", "TENENBAUMS", "TEQUILA", "TERMINATOR", "TEXAS", "THEORY", "THIEF", "THIN", "TIES", "TIGHTS", 
		 "TIMBERLAND", "TITANIC", "TITANS", "TOMATOES", "TOMORROW", "TOOTSIE", "TORQUE", "TOURIST", "TOWERS", "TOWN", 
		 "TRACY", "TRADING", "TRAFFIC", "TRAIN", "TRAINSPOTTING", "TRAMP", "TRANSLATION", "TRAP", "TREASURE", "TREATMENT", 
		 "TRIP", "TROJAN", "TROOPERS", "TROUBLE", "TRUMAN", "TURN", "TUXEDO", "TWISTED", "TYCOON", "UNBREAKABLE", 
		 "UNCUT", "UNDEFEATED", "UNFAITHFUL", "UNFORGIVEN", "UNITED", "UNTOUCHABLES", "UPRISING", "UPTOWN", "USUAL", "VACATION", 
		 "VALENTINE", "VALLEY", "VAMPIRE", "VANILLA", "VANISHED", "VANISHING", "VARSITY", "VELVET", "VERTIGO", "VICTORY", 
		 "VIDEOTAPE", "VIETNAM", "VILLAIN", "VIRGIN", "VIRGINIAN", "VIRTUAL", "VISION", "VOICE", "VOLCANO", "VOLUME", 
		 "VOYAGE", "WAGON", "WAIT", "WAKE", "WALLS", "WANDA", "WAR", "WARDROBE", "WARLOCK", "WARS", 
		 "WASH", "WASTELAND", "WATCH", "WATERFRONT", "WATERSHIP", "WEDDING", "WEEKEND", "WEREWOLF", "WEST", "WESTWARD", 
		 "WHALE", "WHISPERER", "WIFE", "WILD", "WILLOW", "WIND", "WINDOW", "WISDOM", "WITCHES", "WIZARD", 
		 "WOLVES", "WOMEN", "WON", "WONDERFUL", "WONDERLAND", "WONKA", "WORDS", "WORKER", "WORKING", "WORLD", 
		 "WORST", "WRATH", "WRONG", "WYOMING", "YENTL", "YOUNG", "YOUTH", "ZHIVAGO", "ZOOLANDER", "ZORRO", 
		 };

		 title_in = movie_titles[user.nextInt ( 1000 )] + " " + movie_titles[user.nextInt ( 1000 )];
     }  // End of CreateTitle

}
