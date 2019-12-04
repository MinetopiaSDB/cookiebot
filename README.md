# MinetopiaSDB CookieBot  
De MinetopiaSDB CookieBot is een Discord bot die het mogelijk maakt om jouw eigen koekjes-economie te starten in jouw eigen Discord server. Oorspronkelijk was deze bot exclusief voor de [MinetopiaSDB Discord](https://minetopiasdb.nl/discord) maar na de recode is er besloten om de code van de bot publiek te maken.


## Wat kan de bot?
Om de economie levend te houden kunnen mensen met een donator-rol iedere 4 uur koekjes aan andere serverleden geven m.b.v. het !givecookie commando. 

Een functie voor alle serverleden is het !steelcookie commando waarmee ieder serverlid kan proberen koekjes van andere serverleden te stelen. Deze serverleden kunnen ook het !eetcookie commando gebruiken waarmee ze een keer per uur een koekje kunnen opeten in de hoop dat ze hier een prijs voor terug krijgen.

## Help, hoe host ik de bot zelf?
Download de laatste versie van de bot [hier](https://github.com/MinetopiaSDB/cookiebot/releases), of clone de repository en compile 'm zelf. Als je het jar bestand een keer uitvoert krijg je de volgende config:
``` 
Bot:
  Token: TYP-HIER-JOUW-BOTTOKEN
  DonatorRoleID: 381114554010173440
  GuildID: 276296022106308609
  CookieChannelId: -1
MySQL:
  Host: 127.0.0.1
  Port: 3306
  Database: cookiebot
  Username: cookiebot
  Password: ikwilkoekjes
```
Voordat je kunt beginnen moet je een aantal waardes aanpassen in de config. De waardes die je moet aanpassen zijn de bottoken, de MySQL inloggegevens en de bovenstaande IDs aanpassen. Je 
kunt de waarde van de 'CookieChannelId' onaangepast laten, dan werkt de cookiebot in elk kanaal.
