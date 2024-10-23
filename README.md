# call-grapher
In Android, scan through entire call history and collect data on the calls from a number that the user passes in. Then graph out the maxmimum and minimum time of day a call has been recieved given a day of the week, as well as the average time at which there can be a call.

# Steps
1. Installed Android Studio
2. Created LinearLayout
3. Made Button to graph calls
4. Made EditText to input phone number to call
5. Attempted to make Graph
     - Tried importing MPAndroidChart
     - Import syntax is different from examples and my screen
         - == Currently researching why tht is ==
         - Fixed: Looked up what the difference was between my screen and what I found online. Mine is in Goofy while others are in Kotlin. Asked GPT for a fix. Also found out that simply typing the class name in the xml file will autocomplete with the given library if it's working.
      
# Next Steps
- [ ] Fix MPAndroidChart import
- [ ] Add empty graph to xml file
- [ ] OnClick logic to collect phone call data
- [ ] Organize data collected into what the graph needs


# Packages/Things to Install
- MPAndroidChart
- 
