package controller;

import model.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReleaseTagRetriever {

    public List<ReleaseTag> makeTagReleasesList(Repo project, List<Release> listOfJiRelease){

        // Init list of ragged release's objects
        List<ReleaseTag> listOfTagReleases = new ArrayList<>();


        for (Release rlsIndex : listOfJiRelease) {
            String projectRef="release";
            if(project.getApacheProjectName().equals("SYNCOPE")){projectRef=project.getApacheProjectName().toLowerCase();}
            String tagPathName = "refs/tags/" + projectRef + "-" + rlsIndex.getName();

            // Set, Create and Add ReleaseTag objetc to valid tagged release list
            listOfTagReleases.add(new ReleaseTag(
                    tagPathName,                    // Set tag name path
                    rlsIndex,                       // Set Release associated object
                    project                        // Set Repo objects
            ));

            System.out.println("\nAdded Tag: " + rlsIndex.getName());
        }

        return listOfTagReleases;
    }


    public List<ReleaseTag> setBugginess(List<ReleaseTag> tagRelesesToDoThinks, String fileName, List<Release> affectedVersions, String releaseRange){

        // Scroll all file of all Release
        for(ReleaseTag rlsIndex : tagRelesesToDoThinks){
            // Find a ReleaseTag Object that match with one of passed affected version
            if(affectedVersions.stream().anyMatch(o -> rlsIndex.getReleaseFromJira().getName().equals(o.getName()))){
                for(RepoFile rpIndex : rlsIndex.getReferencedFilesList()){
                    // Control for Walk Forward bugginess
                    if(!releaseRange.equals("ALL") && (rlsIndex.getReleaseFromJira().getIndex() >= Integer.parseInt(releaseRange)+1)){break;}

                    // Find that file that it will set buggy and correspond to the passed path
                    if(rpIndex.getNameFile().equals(fileName)){rpIndex.setItsBuggy(true); break;}
                }
            }
        }

        return tagRelesesToDoThinks;
    }

}
