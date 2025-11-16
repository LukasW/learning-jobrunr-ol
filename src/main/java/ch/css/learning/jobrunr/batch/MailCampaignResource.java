package ch.css.learning.jobrunr.batch;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/mail")
@Produces(MediaType.TEXT_PLAIN)
public class MailCampaignResource {

    @Inject
    MailCampaignService mailCampaignService;

    @GET
    @Path("/trigger")
    public String triggerLongJob(@DefaultValue("general-template") @QueryParam("template") String mailTemplateKey) {
        mailCampaignService.startMailCampagneBatch(mailTemplateKey);
        return "Batch Job eingereiht!";
    }
}
