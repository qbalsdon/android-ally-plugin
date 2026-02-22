# Android Ally Plugin (AAP) for Android Studio

The [Trello Board][4] (deprecated) will show you the project roadmap, any issues in the GitHub tracker should also be there.

This plugin will only ever support [TalkBack for Developers][9] and not any other screen reader without ADB-friendly commands or can receive ADB input commands (`adb shell input keyevent ...` and `adb shell input keycombination`) [while there is still a bug on the Android operating system][12], this is impossible.

[The code is open source][8]. This includes Google's TalkBack, Samsung's TalkBack, Amazon VoiceView and CSR - unless they add ADB features, in which case they will be given a separate tab. Separation of concerns is the highest priority in this code.

## This is in Swing, ew!
Yes it's written in Swing, you are more than welcome to create a [Jewel][5] version if you like. The main reasons the project is not using Jetpack Compose yet are:
1. It's not production ready
2. Documentation is scarce, I couldn't even create a template project (not that I am all that great, I'm just on a late deadline)
3. APIs are not settled
4. Performance concerns: Jetpack Compose is optimised for mobile
5. Accessibility concerns, Swing is at least old enough for AT to do some tricks

## Purpose of the project
- Encourage developers to make their apps and features accessible
- Consolidate the Android accessibility services and settings into a simple-to-use interface
- Increase awareness of different accessibility settings
- Help developers use either a physical device or emulator
- Reduce developer context switching while conducting manual tests
- Lower the barrier to entry in getting comfortable using screen readers

## Scope of the project
- A plugin for Android studio
- A user interface to various assistive technologies
- Simple check list for accessibility standards [Future work]

## Conduct and Contributing
This plugin was built using the [JetBrains IntelliJ Platform SDK][1]. Best practice will be taken as much as possible from that document and [SOLID programming principles][2].

Please ensure you have read and abide by the [JetBrains developer code of conduct][3]

### PR's will be accepted if
* You are positively contributing to the source code
* You can articulate the reasons for your changes 
* Reasonable tests have been written for the feature
* The PR has screenshots if there are UI changes

### PR's will not be accepted if
* You are rude, discriminatory or demeaning in any way
* You use object classes or companion objects ([find out why][0])

## ADB in more detail

```
adb shell service list                                      # get system services and interfaces
adb shell dumpsys settings                                  # list all elements of a service (e.g. settings can do any service)
https://cs.android.com/android/platform/superproject/main   # search for interface (no package + ".aidl" suffix)
adb shell service call uimode 6 i32 2                       # call 6th method of IUiModeManager.aidl with int param (can't do java obj params)
```
Thanks to [Chris Simmons][7] and his amazing [DroidCon presentation "Exploring Android Internals with ADB"][6]

## Signing Locally

Follow the [instructions to generate a private key and chain file][10]
You'll need to set up 4 environment variables:
```bash
export VERSION_NUMBER=[VERSION NUMBER]
export CERTIFICATE_CHAIN=$(cat chain.crt)
export PRIVATE_KEY=$(cat private.pem)
export PRIVATE_KEY_PASSWORD=[YOUR PASSWORD]
```

And then you can run `./gradlew signPlugin` - I have not been successful [running this from the IDE][11], but you're welcome to try.

Using this method you will not be able to upload to the store, but this is how you can test locally.

[0]: https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension
[1]: https://plugins.jetbrains.com/docs/intellij/welcome.html
[2]: https://en.wikipedia.org/wiki/SOLID
[3]: https://github.com/jetbrains#code-of-conduct
[4]: https://trello.com/b/kSW3T8yG/android-ally-plugin-for-android-studio
[5]: https://github.com/JetBrains/jewel
[6]: https://www.droidcon.com/2022/11/15/exploring-android-internals-with-adb/
[7]: https://www.linkedin.com/in/chrisdsimmonds/
[8]: https://github.com/qbalsdon/talkback/tree/main
[9]: https://ally-keys.com/tb4d.html
[10]: https://plugins.jetbrains.com/docs/intellij/plugin-signing.html#signing-methods
[11]: https://plugins.jetbrains.com/docs/intellij/plugin-signing.html#provide-secrets-to-ide
[12]: https://issuetracker.google.com/issues/188719933