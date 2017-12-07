package net.devosmium.adhubbot;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;

import java.util.EnumSet;
import java.util.List;

/**
 * Created and maintained by Dev_Osmium on 13-Nov-17 at 9:37 PM
 */
public class GuildCreateHandler {

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        try {
            EnumSet<Permissions> mutedPermissions = EnumSet.of(Permissions.SEND_MESSAGES);
            EnumSet<Permissions> allowMutedPermissions = EnumSet.of(Permissions.READ_MESSAGES);
           IRole mutedRole =  event.getGuild().createRole();
           mutedRole.changeName(BotUtils.RECOG_NAME + "-Mute");
           List<IChannel> channelList = event.getGuild().getChannels();
           for (int i = 0; i < channelList.size(); i++) {
                channelList.get(i).overrideRolePermissions(mutedRole, allowMutedPermissions, mutedPermissions);
           }
           SQLiteUtils.createServerSettingsTable(event.getGuild().getStringID());
           SQLiteUtils.insertDefaultServerSettings(event.getGuild().getLongID());

        } catch (DiscordException e) {
            e.printStackTrace();
        }


    }

}
