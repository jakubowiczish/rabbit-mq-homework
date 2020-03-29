Zadanie polega na implementacji, 
z użyciem RabbitMQ, systemu pośredniczącego pomiędzy agencjami kosmicznymi (Agencja),
a dostawcami usług transportu kosmicznego (Przewoźnik). 

Agencje kosmiczne zlecają wykonanie trzech typów usług: 
- przewóz osób
- przewóz ładunku
- umieszczenie satelity na orbicie

W związku z podpisanym porozumieniem, 
obowiązują następujące zasady współpracy:
- ceny poszczególnych usług są takie same u wszystkich Przewoźników, w związku z czym nie są uwzględniane w systemie
- każdy Przewoźnik świadczy dokładnie 2 z 3 typów usług - przystępując do współpracy określa które 2 typy usług świadczy
- konkretne zlecenie na wykonanie danej usługi powinno trafić do pierwszego wolnego Przewoźnika, który obsługuje ten typ zlecenia
- dane zlecenie nie może trafić do więcej niż jednego Przewoźnika
- zlecenia identyfikowane są przez nazwę Agencji oraz wewnętrzny numer zlecenia nadawany przez Agencję
- po wykonaniu usługi Przewoźnik wysyła potwierdzenie do Agencji

W wersji premium tworzonego systemu dostępny 
jest dodatkowy moduł administracyjny. 
Administrator dostaje kopię wszystkich wiadomości 
przesyłanych w systemie oraz ma możliwość wysłania 
wiadomości w trzech trybach:
- do wszystkich Agencji
- do wszystkich Przewoźników
- do wszystkich Agencji oraz Przewoźników

Dostosowanie się do unijnych wymagań 
w zakresie oprogramowania dla przemysłu kosmicznego wymaga,
aby do projektu załączona została dokumentacja 
w postaci schematu działania systemu. 
Schemat powinien uwzględniać:
- użytkowników, exchange'e, kolejki, klucze użyte przy wiązaniach
- schemat musi mieć postać elektroniczną, nie może to być skan odręcznego rysunku

Scenariusz prezentacji zadania:
- 2 agencje kosmiczne
- 2 przewoźników, z których jeden obsługuje przewóz osób oraz ładunków, a drugi przewóz ładunków oraz umieszczenie satelity na orbicie
- 1 administrator
- w ramach testów systemu przyjmujemy, że zlecenia obsługiwane są natychmiast

Punktacja:
- schemat: 2 punkty
- obsługa Agencji oraz Przewoźników: 5 punktów
- moduł do administracji: 3 punkty

Zadanie można zrealizować w dowolnym 
języku programowania wspieranym przez RabbitMQ