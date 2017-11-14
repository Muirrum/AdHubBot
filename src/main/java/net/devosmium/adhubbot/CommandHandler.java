package net.devosmium.adhubbot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;

public class CommandHandler {

    // A static map of commands mapping from command string to the functional impl
    private static Map<String, Command> commandMap = new HashMap<>();
    static CharSequence[] bannedWords = {
            "damn",
            "shit",
            "nigger",
            "cunt",
            "dick",
            "Alcoholic",
            "Amateur",
            "Analphabet",
            "Anarchist",
            "Arse",
            "Arselicker",
            "Ass",
            "Ass master",
            "Ass-kisser",
            "Ass-nugget",
            "Ass-wipe",
            "Asshole",
            "Balls",
            "Barbar",
            "Bastard",
            "Bastard",
            "Beavis",
            "Biest",
            "Bitch",
            "Blubber gut",
            "Bogeyman",
            "Booby",
            "Boozer",
            "Bozo",
            "Brain-fart",
            "Brainless",
            "Brainy",
            "Brontosaurus",
            "Brownie",
            "Bugger",
            "Bugger, silly",
            "Bulloks",
            "Bum",
            "Bum-fucker",
            "Butt",
            "Buttfucker",
            "Butthead",
            "Callboy",
            "Callgirl",
            "Children fucker",
            "Clit",
            "Cock",
            "Cock master",
            "Cock up",
            "Cockboy",
            "Cockfucker",
            "Cockroach",
            "Coky",
            "Con merchant",
            "Con-man",
            "Country bumpkin",
            "Cunt",
            "Cunt sucker",
            "Deathlord",
            "Derr brain",
            "Devil",
            "Dickhead",
            "Disguesting packet",
            "Diz brain",
            "Do-Do",
            "Dog, dirty",
            "Dogshit",
            "Drinker",
            "Drunkard",
            "Dufus",
            "Dulles",
            "Dumbo",
            "Dummy",
            "Dumpy",
            "Fatso",
            "Fellow",
            "Fibber",
            "Freak",
            "Fuck",
            "Fuck face",
            "Fuck head",
            "Fuck noggin",
            "Fucker",
            "Hell dog",
            "Hillbilly",
            "Hippie",
            "Homo",
            "Homosexual",
            "Hooligan",
            "Horse fucker",
            "Idiot",
            "Ignoramus",
            "Jack-ass",
            "Jerk",
            "Lard face",
            "Latchkey child",
            "Liar",
            "Looser",
            "Lucky",
            "Lumpy",
            "Luzifer",
            "Macho",
            "Macker",
            "Man, old",
            "Minx",
            "Missing link",
            "Monkey",
            "Monster",
            "Motherfucker",
            "Mucky pub",
            "Mutant",
            "Neanderthal",
            "Nerfhearder",
            "Nurd",
            "Nuts, numb",
            "Oddball",
            "Oger",
            "Oil dick",
            "Old fart",
            "Orang-Uthan",
            "Original",
            "Outlaw",
            "Pack",
            "Pain in the ass",
            "Pavian",
            "Pencil dick",
            "Pervert",
            "Pirate",
            "Pornofreak",
            "Prick",
            "Prolet",
            "Queer",
            "Querulant",
            "Rat",
            "Rat-fink",
            "Reject",
            "Retard",
            "Riff-Raff",
            "Ripper",
            "Roboter",
            "Rowdy",
            "Rufian",
            "Sack",
            "Sadist",
            "Saprophyt",
            "Satan",
            "Schfincter",
            "Shark",
            "Shit eater",
            "Shithead",
            "Simulant",
            "Skunk",
            "Skuz bag",
            "Slave",
            "Sleeze",
            "Sleeze bag",
            "Slimer",
            "Slimy bastard",
            "Small pricked",
            "Snail",
            "Snake",
            "Snob",
            "Snot",
            "Son of a bitch",
            "Square",
            "Stinker",
            "Stripper",
            "Stunk",
            "Swindler",
            "Swine",
            "Teletubby",
            "Thief",
            "Toilett cleaner",
            "Tussi",
            "Typ",
            "Vampir",
            "Vandale",
            "Varmit",
            "Wallflower",
            "Wanker",
            "Wanker, bloody",
            "Weeze Bag",
            "Whore",
            "Wierdo",
            "Wino",
            "Witch",
            "Womanizer",
            "Woody allen",
            "Worm",
            "Xenophebe",
            "XXX Watcher",
            "XXX",
            "Yak",
            "Yeti",
            "Zit face",
    };
    private final boolean noisyDebug = false;

    // Statically populate the commandMap with the intended functionality
    // Might be better practise to do this from an instantiated objects constructor
    static {

        commandMap.put("testcommand", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "This is how messages *should* look!", "Example!", event, true);
        });

        commandMap.put("ping", (event, args) -> {
            BotUtils.sendMessage(event.getChannel(), "pong", "Pong!", event, true);
        });
        commandMap.put("shutdown", (event, args) -> {
           if (!event.getAuthor().getStringID().equals("118455061222260736")){
               BotUtils.sendMessage(event.getChannel(), "Nice try m8", "u wot m8?", event, false);
           } else if (event.getAuthor().getStringID() == "118455061222260736") {
               System.exit(0x00001);
           }
           });
        commandMap.put("sendEmbed", (event, args) -> {
            if (event.getAuthor().getStringID().equals("118455061222260736")) {
                BotUtils.sendMessage(event.getChannel(), args.toString(), "Message from " + BotUtils.OWNER_RECOG_NAME, event,
                 true);
            }
        });
        commandMap.put("ban", (event, args) -> {
           if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.BAN) || event.getAuthor().getStringID().equals("118455061222260736")) {
              List<IUser> mentionArray = event.getMessage().getMentions();

              event.getGuild().banUser(mentionArray.get(0), args.toString());
           }
        });
        commandMap.put("mute", (event, args) -> {
           if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER) || event.getAuthor().getStringID().equals("118455061222260736")) {
               List<IUser> mentionList = event.getMessage().getMentions();
               BotUtils.muteUser(mentionList.get(0), event.getGuild());
           }
        });
           }




    @EventSubscriber
    public void onMessageReceived (MessageReceivedEvent event) {
        // This whole FOR loop is the badwords filter.
        for (int i = 0; i < bannedWords.length; i++) {
            if (noisyDebug) {
                System.out.println("I didn't bork the FOR loop!");
            }
            if (event.getMessage().getContent().contains(bannedWords[i])) {
                try {
                    event.getMessage().delete();
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor().mention() + "Watch your language!",
                            "Warning", event, false);
                } catch (MissingPermissionsException e) {
                    System.out.println("Missing Permission MANAGE_MESSAGEs in guild with ID "
                            + event.getGuild().getStringID() + " and name " + event.getGuild().getName());
                    BotUtils.sendMessage(event.getChannel(), "I'm missing the permission MANAGE_MESSAGES in" +
                            " this guild, please ask a moderator to add it to me.", "Error", event, false);
                }
            }
        }
        // Note for error handling, you'll probably want to log failed commands with a logger or sout
        // In most cases it's not advised to annoy the user with a reply incase they didn't intend to trigger a
        // command anyway, such as a user typing ?notacommand, the bot should not say "notacommand" doesn't exist in
        // most situations. It's partially good practise and partially developer preference

        // Given a message "/test arg1 arg2", argArray will contain ["/test", "arg1", "arg"]
        String[] argArray = event.getMessage().getContent().split(" ");

        // First ensure at least the command and prefix is present, the arg length can be handled by your command func
        if (argArray.length == 0)
            return;

        // Check if the first arg (the command) starts with the prefix defined in the utils class
        if (!argArray[0].startsWith(BotUtils.PREFIX))
            return;

        // Extract the "command" part of the first arg out by just ditching the first character
        String commandStr = argArray[0].substring(1);

        // Load the rest of the args in the array into a List for safer access
        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0); // Remove the command

        // Instead of delegating the work to a switch, automatically do it via calling the mapping if it exists

        if (commandMap.containsKey(commandStr))
            commandMap.get(commandStr).runCommand(event, argsList);


    }
}