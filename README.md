# Professor Doge
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/mitchtalmadge/professor-doge/master/LICENSE)
[![Build Status](https://travis-ci.org/MitchTalmadge/Professor-Doge.svg?branch=master)](https://travis-ci.org/MitchTalmadge/Professor-Doge)
[![GitHub issues](https://img.shields.io/github/issues/MitchTalmadge/Professor-Doge.svg)](https://github.com/MitchTalmadge/Professor-Doge/issues)

<img src="http://i.imgur.com/KdF2tKJ.png" width="140px" align="left"/>

Professor Doge is a silly Discord bot with an emphasis on cryptocurrency information. 

The bot acts like a user on a Discord server, to which users can send commands and receive
responses. For example, Professor Doge can report on the prices of multiple cryptocurrencies
from multiple different exchanges.

This was created for fun to embellish my favorite [Cryptocurrency Discord Server](https://discord.gg/FjZNXfK).

## Architecture
Professor Doge is build upon the [Spring Boot](https://github.com/spring-projects/spring-boot) framework. 
Spring Boot provides multiple critical components of the bot, including scheduled 
(cron) jobs to fetch and cache the current prices of cryptocurrencies. 

Hosting is provided by [Heroku](https://www.heroku.com/) on a free tier dyno.

