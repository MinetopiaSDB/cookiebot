# MinetopiaSDB CookieBot  
De MinetopiaSDB CookieBot is een Discord bot die het mogelijk maakt om jouw eigen koekjes-economie te starten in jouw
eigen Discord server. Oorspronkelijk was deze bot exclusief voor de [MinetopiaSDB Discord](https://minetopiasdb.nl/discord) maar na de recode is er
besloten om de code van de bot beschikbaar te stellen.

## Wat kan de bot?
Om de economie levend te houden kunnen mensen met een donator-rol iedere 4 uur koekjes aan andere serverleden geven met
het `/givecookie` commando.

Een functie voor alle serverleden is het `/steelcookie` commando waarmee ieder serverlid een poging kan wagen om koekjes
van andere serverleden te stelen. Deze serverleden kunnen ook het `/eetcookie` commando gebruiken om een keer per uur een
koekje kunnen eten, in de hoop dat ze hier een prijs voor terug krijgen.

## Hoe werken de aandelen?
Het is nodig om een API key van Finnhub te hebben om aandelenkoersen op te vragen. Deze API key kan je [hier](https://finnhub.io/)
gratis aanvragen. De huidige prijzen van aandelen worden iedere 3 minuten geüpdatet en afgerond op gehele koekjes, op deze
manier kun je met jouw koekjes direct aandelen verhandelen.


## Help, hoe host ik de bot zelf?
Je kunt de laatste release van de bot [hier](https://github.com/MinetopiaSDB/cookiebot/releases) downloaden. Je kunt de
Discord bot ook als Docker container hosten. Zie daarvoor de [package pagina](https://github.com/MinetopiaSDB/cookiebot/pkgs/container/cookiebot).

Voordat je kunt beginnen moet je een aantal waardes aanpassen in het jouw environment (of `.env` bestand). Het is nodig
om alle waardes in te stellen, m.u.v. `COOKIE_CHANNEL_ID`. Als deze op -1 gelaten wordt, werkt de cookiebot in elk kanaal.
Om de bot te gebruiken moet je ook de ``Server Members Intent`` aanzetten in jouw Discord Developer Portal.
