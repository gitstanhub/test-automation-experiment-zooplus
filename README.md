## Test automation project for Zooplus

<p align="center">
<br>
  <img src="media/zooplus_logo.png" alt="spotify-logo" width="400">
</p>

## Table of Contents
- [Overview](#overview)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)

## Overview
This is a test automation suite covering the functionality of [Cart Page](https://www.zooplus.com/checkout/cart) from Zooplus online shop.  
It supports replacement of Sid cookie with custom value for personalisation of test runs using `@SidCookie` annotation.

### Technology stack
* Java
* Selenide
* Gradle
* JUnit 5
* Allure

## Installation and Setup

### General:
* Clone the repo to a local directory: <br>
```zsh
git clone https://github.com/gitstanhub/test-automation-zooplus.git
```

* Make sure to have Java installed on your local machine

## Usage
To run the test suite from the project root directory, first generate the Gradle executables once:
```zsh
gradle wrapper
```
Then run with:
```zsh
./gradlew clean test --tests CartTests
```

To generate Allure report with the results from the root directory:
```zsh
allure serve build/allure-results
```
