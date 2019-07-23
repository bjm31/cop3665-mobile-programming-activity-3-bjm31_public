# Activity 3

## Purpose
The purpose of this activity is twofold. It ensures that you have worked through the videos of the previous week (understanding how to deal with web services is important for modern development) and it gives you some familiarity with a common problem, paging.  

## Description
The activity is to add paging to your application. If you go through the Cat API's information, you will find that the API also returns information about the number of total query matches allows you to get results based on page and a limit. Currently, we are only displaying the first page of results. Extend the application to allow for the next page of results to replace the current results whenever the user has completely scrolled the RecyclerView. Preferably, this would be done with an effect such as infinite scrolling (results append instead of replace), however you are not required to implement infinite scrolling. In order to complete this activity, you will want to add an OnScrollListener to your RecyclerView and implement the OnScrolled method.  

## Submitting work
Once your program is working as expected, in Android Studio you need to create a commit and push the assignment as shown in the video.

I will pull all of the repositories from Github sometime after the deadline. This is the same for projects. 
Commit and push to origin early and often each time you get a block of code working as desired.
When I pull down the code after the deadline, it will pull down the most recent version of the code not your partially completed version(s).
This is also useful if you are stuck and need my help, you can push your code to Github and I can pull it onto my machine to take a look at it.
