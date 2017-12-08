package net.devosmium.adhubbot;

public class Application {

    public static void createApplication(int id, String inviteCode, String applicant, String applicantId, String serverName) {
        SQLiteUtils.applyForPartner(id, serverName, applicant, applicantId, inviteCode);
    }

}
