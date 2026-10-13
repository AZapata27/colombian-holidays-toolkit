# Colombian Holiday Toolkit

[![CI](https://github.com/AZapata27/colombian-holidays-toolkit/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/AZapata27/colombian-holidays-toolkit/actions/workflows/ci.yml)

## Overview

Colombian Holiday Toolkit is a Java library designed to simplify working with Colombian public holidays. Whether you're building HR software, scheduling applications, or any system that needs to account for Colombian holidays, this toolkit provides easy-to-use methods for various holiday-related operations.

## Features

- Verify if a given date is a Colombian holiday
- Find the next upcoming holiday from a given date
- Count holidays in a specific month or year
- Get a list of all holidays for a given year
- Provide descriptions for each holiday

## Architecture

`ColombianHolidayValidator` implements the generic `HolidayValidator<T>` API. It loads the holiday
definitions from `ColombianHolidayFactory` and resolves the actual dates for a year: fixed holidays
keep their date, Easter-based ones are offset from the Easter Sunday given by `EasterCalculator`,
and transferable ones are moved to the following Monday (Law 51 of 1983, "Ley Emiliani").

```mermaid
flowchart LR
    Client["Client code"] --> API["HolidayValidator&lt;T&gt;<br/>(interface)"]
    API --> Validator["ColombianHolidayValidator"]
    Validator --> Factory["ColombianHolidayFactory<br/>holiday definitions"]
    Factory --> Fixed["Fixed<br/>Jan 1, May 1, Jul 20, Aug 7, Dec 8, Dec 25"]
    Factory --> Easter["Easter-based<br/>Holy Thursday, Good Friday,<br/>Ascension, Corpus Christi, Sacred Heart"]
    Factory --> Transferable["Transferable<br/>Jan 6, Mar 19, Jun 29, Aug 15,<br/>Oct 12, Nov 1, Nov 11"]
    Easter --> EasterCalc["EasterCalculator<br/>(Meeus/Jones/Butcher)"]
    Easter -- "Ascension, Corpus, Sacred Heart" --> Monday["Move to next Monday<br/>(Ley Emiliani)"]
    Transferable --> Monday
    Fixed --> Result
    EasterCalc --> Result
    Monday --> Result["Sorted results<br/>List&lt;LocalDate&gt; / List&lt;ColombianHoliday&gt;<br/>isHoliday, next / previous, long weekend"]
```

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.azapata27</groupId>
    <artifactId>colombian-holiday-toolkit</artifactId>
    <version>1.0.0</version>
</dependency>
```
