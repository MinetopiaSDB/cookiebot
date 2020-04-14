# MinetopiaSDB CookieBot  
De MinetopiaSDB CookieBot is een Discord bot die het mogelijk maakt om jouw eigen koekjes-economie te starten in jouw eigen Discord server. Oorspronkelijk was deze bot exclusief voor de [MinetopiaSDB Discord](https://minetopiasdb.nl/discord) maar na de recode is er besloten om de code van de bot publiek te maken.


## Wat kan de bot?
Om de economie levend te houden kunnen mensen met een donator-rol iedere 4 uur koekjes aan andere serverleden geven m.b.v. het !givecookie commando. 

Een functie voor alle serverleden is het !steelcookie commando waarmee ieder serverlid kan proberen koekjes van andere serverleden te stelen. Deze serverleden kunnen ook het !eetcookie commando gebruiken waarmee ze een keer per uur een koekje kunnen opeten in de hoop dat ze hier een prijs voor terug krijgen.

Sinds versie 2.2 bevat de bot ook ondersteuning voor aandelen, deze aandelen worden iedere 3 minuten automatisch geüpdatet en afgerond op gehele koekjes. Meer uitleg is te vinden bij het kopje hieronder.

## Hoe werken de aandelen?
Je kunt aandelen aanzetten door bij het kopje ``Aandelen`` enabled op ``true`` te zetten, als je dit gedaan hebt moet je jouw Finnhub API key in de config neerzetten. Deze API key kan je [hier](https://finnhub.io/) gratis aanvragen. 
De aandelen worden automatisch iedere 3 minuten geüpdatet en afgerond op gehele koekjes, op deze manier kun je met jouw koekjes direct aandelen kopen en deze later weer verkopen, met de bedoeling om hier winst op te maken. 

Je kunt zelf aandelen toevoegen door bij het kopje Stock: een nieuwe regel toe te voegen met de ticker symbol van het aandeel en de volledige naam.

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
Aandelen:
  Enabled: false
  FinnhubAPIKey: LEUKEAPIKEYZEG
  Stock:
    AMD: Advanced Micro Devices
    AMZN: Amazon.com
    AAPL: Apple Inc.
    TSLA: Tesla, Inc.
    MSFT: Microsoft
    NVDA: NVIDIA
```
Voordat je kunt beginnen moet je een aantal waardes aanpassen in de config. De waardes die je moet aanpassen zijn de bottoken, de MySQL inloggegevens en de bovenstaande IDs aanpassen. Je 
kunt de waarde van de 'CookieChannelId' onaangepast laten, dan werkt de cookiebot in elk kanaal.
