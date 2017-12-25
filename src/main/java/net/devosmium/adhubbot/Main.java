package net.devosmium.adhubbot;

import sx.blah.discord.api.IDiscordClient;

import java.io.File;


public class Main {
    
    public static void main(String[] args) {
        //Initialize DB
        String filePathString = "data.db";
        File f = new File(filePathString);
        if(f.exists() && !f.isDirectory()) {
            SQLiteUtils.createDB();
        }
        // Create Partner Application Table
        SQLiteUtils.createPartnerApplicationsTable();
        //Initialize Client
        if (args.length != 1) {
            System.out.println("Please put the discord app token as ");
            return;
        }
        
         IDiscordClient cli = BotUtils.getBuiltDiscordClient(args[0]);


        cli.getDispatcher().registerListener(new CommandHandler());
        cli.login();
        
        
    }
    
}
