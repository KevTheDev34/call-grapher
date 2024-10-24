# call-grapher
In Android, scan through entire call history and collect data on the calls from a number that the user passes in. Then graph out the maxmimum and minimum time of day a call has been recieved given a day of the week, as well as the average time at which there can be a call.

# Steps
1. Installed Android Studio
2. Created LinearLayout
3. Made Button to graph calls
4. Made EditText to input phone number to graph
5. Attempted to make Graph
     - Tried importing MPAndroidChart
     - Import syntax is different from examples and my screen
         - == Currently researching why tht is ==
         - Fixed: Looked up what the difference was between my screen and what I found online. Mine is in Goofy while others are in Kotlin. Asked GPT for a fix. Also found out that simply typing the class name in the xml file will autocomplete with the given library if it's working.
6. How to get phone data
     - https://stackoverflow.com/questions/57313691/how-do-i-get-the-latest-call-logs-in-android-studio
     - Got ^ as skeleton code, will go through and adjust to my needs
     - Refactored into modern Kotlin code (with GPT)
7. Set OnClick listener to run getCallLogs (function for retrieveing call logs)
     - Printing call logs
8. Set up Pixel to retrieve actual call logs

# Next Steps
- [x] Fix MPAndroidChart import
- [x] Add empty graph to xml file
- [x] OnClick logic to collect phone call data
- [ ] Organize data collected into what the graph needs


# Packages/Things to Install
- MPAndroidChart
- 
