# E-mobility Kompanija

## Opis

ePJ2 je e-mobility kompanija koja se bavi iznajmljivanjem električnih automobila, bicikala i trotineti na užem i širem prostoru grada Java. Cilj ovog projektnog zadatka je razviti program koji simulira korišćenje prevoznih sredstava na osnovu predefinisanih podataka i generiše detaljne finansijske obračune, statistike, te prati stanja svih prevoznih sredstava koji se koriste.

## Podaci o prevoznim sredstvima

### Automobili

Za električne automobile čuvaju se sljedeći podaci:
- Jedinstveni identifikator (ID)
- Datum nabavke
- Cijena nabavke
- Proizvođač
- Model
- Opis
- Trenutni nivo baterije

### Bicikli

Za električne bicikle čuvaju se sljedeći podaci:
- Jedinstveni identifikator (ID)
- Proizvođač
- Model
- Cijena nabavke
- Trenutni nivo baterije
- Domet sa jednim punjenjem (autonomija)

### Trotineta

Za električne trotinete čuvaju se sljedeći podaci:
- Jedinstveni identifikator (ID)
- Proizvođač
- Model
- Trenutni nivo baterije
- Cijena nabavke
- Maksimalna brzina

## Funkcionalnosti

### Iznajmljivanje Prevoznih Sredstava

Osnovni posao kompanije je iznajmljivanje prevoznih sredstava. Prilikom iznajmljivanja evidentiraju se:
- Datum i vrijeme iznajmljivanja
- Ime korisnika
- Trenutna lokacija preuzimanja
- Lokacija ostavljanja prevoznog sredstva
- Trajanje korišćenja u sekundama

Automobili imaju dodatne zahtjeve, uključujući dostavljanje identifikacionog dokumenta (pasoš ili lična karta) i vozačke dozvole.

### Finansijski Obračun

Cijene iznajmljivanja svakog tipa prevoznog sredstva se definišu po vremenu korišćenja u sekundama, uz dodatne faktore koji utiču na cijenu, uključujući:
- Udaljenost
- Kvarove
- Popuste
- Promocije

Sve stavke se navode na računima. U slučaju kvara, ukupno za plaćanje iznosi 0.

### Izvještaji

Program generiše dva tipa izvještaja:
1. **Sumarni Izvještaj**
   - Ukupan prihod
   - Ukupan popust
   - Ukupan iznos za održavanje
   - Ukupan iznos za popravke kvarova
   - Ukupni troškovi kompanije
   - Ukupan porez

2. **Dnevni Izvještaj**
   - Stavke grupisane po datumu.

## Grafički Interfejs

Program sadrži nekoliko grafičkih interfejsa, uključujući:
- Glavni ekran za prikaz mape
- Ekran za prikaz svih prevoznih sredstava
- Ekran za prikaz kvarova
- Ekran za prikaz rezultata poslovanja

### Mapa

Mapa se sastoji od 20x20 polja, pri čemu su polja označena bijelom bojom širi dio grada, a plavom bojom uži dio grada. Putanje su isključivo pravolinijske, a simulacija se obavlja po redu iznajmljivanja.

## Dodatne Funkcionalnosti

1. Pronalaženje prevoznih sredstava koja su donijela najviše prihoda za svaku vrstu.
2. Pronalaženje prevoznih sredstava koja su donijela najviše gubitaka za svaku vrstu.
3. Pronalaženje prevoznih sredstava koja su se pokvarila i cijene popravki.

Ovi podaci se serijalizuju u binarne fajlove, a program omogućava deserijalizaciju i prikaz podataka.

## Kako Pokrenuti

1. Preuzmite repozitorij.
2. Konfigurišite `properties` fajlove prema vašim potrebama.
3. Pokrenite `Main` klasu.
