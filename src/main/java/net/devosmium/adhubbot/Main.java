package net.devosmium.adhubbot;

import sx.blah.discord.api.IDiscordClient;

public class Main {
    
    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Please put the discord app token as ");
            return;
        }
        
         IDiscordClient cli = BotUtils.getBuiltDiscordClient(args[0]);


        cli.getDispatcher().registerListener(new CommandHandler());
        cli.getDispatcher().registerListener(new GuildCreateHandler());
        cli.login();
        
        
    }
    
}
