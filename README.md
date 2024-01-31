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
It supports replacement of SID cookie with a custom value using `@SidCookie` annotation for personalisation of test runs.

### Technology stack
* Java
* Selenide, Selenium
* Gradle
* JUnit 5
* Allure

## Installation and Setup

### General:
* Clone the repo to a local directory: <br>
```zsh
git clone https://github.com/gitstanhub/test-automation-zooplus.git
```

* Make sure to have Java installed on your machine

## Usage
To run the test suite from the project root directory using Gradle Wrapper:
```zsh
./gradlew clean test --tests CartTests
```
... or using Gradle if it's installed on your machine:
```zsh
gradle clean test --tests CartTests
```

To generate Allure report with the results from the root directory:
```zsh
allure serve build/allure-results
```
