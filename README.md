# e2e-scenario

## After checkout

    $ nvm use # optional
    $ npm install
    $ grunt download-electron # you need grunt-cli for that, if you don't have it `npm install -g grunt-cli`
    $ lein externs > app/js/externs.js
    $ lein cljsbuild once main
    $ lein figwheel frontend

At this point the app is built, you can run it (in separate terminal) using:

    $ ./electron/electron app

It should show a counter and a button that you can use to increment it. Exit
the app when you're ready to go further.

## Building & runnting the tests

You need [chromedriver](https://sites.google.com/a/chromium.org/chromedriver/) to
instrument electron using selenium. It's just a standalone binary. You can
[get it here](http://chromedriver.storage.googleapis.com/index.html?path=2.21/).

After download just enter the directory where you extracted the archive and
run `chromedriver` like so:

    $ chromedriver --url-base=wd/hub --port=9515

Now you are ready to build and run the tests. To build the tests do:

    $ lein cljsbuild once e2e

To run the tests:

    $ node target/test_e2e/main.js

You should see app window appear two times on the screen and some things
happening. After that you will see a test report in your terminal.

## How is this code structured.

`src`, `src_tools` and `src_frontend` are related to the electron app, and if you're only
interested in integration tests you can ignore them completely. Interesting stuff related to
tests is in `test_e2e` directory.

`cljs-webdriver-scenario.scenario` namespace contains some utility functions and macros
that are useful when running the tests. It could be possibly extracted into a separate
library (that is 45 lines of code...).

`e2e-scenarios.runner` namespace contains the test runner, and contains per-project
configuration for running the tests via nodejs.

`e2e-scenarios.counter-test` contains 2 tests. These two test do exactly the same thing/
`test-counter-using-deftest` is there to show you how your test could look like if you
would like to express it in terms of `deftest` only. `test-counter-using-scenario`
is there to show you how *functionally same test* can be expressed much cleaner using
`defscenario` macro.

## What now?

Play with the tests if you feel like it. Make them crash by changing the assertion
condition and watch what happens. Make them crash by using invalid css selector and
see what happens.

## Todo?

A lein plugin that would automate all 3 steps described in "Building and running
the tests" would probably be nice.

Maybe some support for `:before` and `:after` test setup? I am not sure if we
need that.


## License

Copyright ©  FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
