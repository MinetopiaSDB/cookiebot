# MinetopiaSDB CookieBot  
De MinetopiaSDB CookieBot is een Discord bot die het mogelijk maakt om jouw eigen koekjes-economie te starten in jouw eigen Discord server. Mensen met een donator-rol kunnen iedere 
4 uur koekjes aan andere serverleden geven m.b.v. het !givecookie commando. Normale serverleden kunnen m.b.v. het !steelcookie commando proberen koekjes van andere server leden te 
stelen. Ze kunnen ook door !eetcookie te gebruiken ieder uur 1 koekje opeten in de hoop dat je hier dan een prijs voor terug krijgt.

## Help, hoe host ik de bot zelf?
Je moet de bot compilen op Java 13 om een bruikbare executable te krijgen. Als je deze executable een keer uitvoerd krijg je de volgende config:
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