## Autorid
Nimed: Jan Markus Salum, Andry Avamägi

## Projekti Põhjalik Kirjeldus
Programmi eesmärk on leida parimad hinnad erinevatele toidutoodetele, kasutades otsingusõnu ning kaabitsedes Rimi ja Coopi poest toodete nimed, hinnad ja kilohinnad asünkroonselt. Seejärel sorteerib programm tooted kilohinna järgi ning sõltuvalt kasutaja sisendist väljastab kõik erinevad tooted, mis sobisid otsingusõnaga, või iga toote odavaima variandi. Vastused esitatakse ilusti visuaalselt ekraanile.

**Kasutusjuhis:** Pärast programmi käivitamist sisestage kasutajasisendisse:
- Üks toode (näiteks "kodujuust") -> väljastab kõik kodujuustud Rimi ja Coopi sortimendist ning sorteerib need kilohinna järgi.
- Mitu toodet, mis on eraldatud koma või semikooloniga (näiteks "kurk, tomat, pelmeenid, kartulikaste") -> väljastab iga toote odavaima variandi (kilogrammi hinna järgi).

## Klassid

### Peaklass
- **Eesmärk:** Peaklass, mille kasutaja käivitab.

### Abstraktne Klass Kaabitseja
- **Eesmärk:** Abstraktne klass, mida coop ja rimi klass extendivad ja mis sisaldab funktsiooni `kaabitse`, mis tagastab ArrayListi toodetest vastavalt otsingusõnele.

### Klass COOP
- **Eesmärk:** Extendib Kaabitsejat, kaabitsedes COOPi veebipoest.

### Klass Rimi
- **Eesmärk:** Extendib Kaabitsejat, kaabitsedes Rimi veebipoest.

### Klass Toode
- **Eesmärk:** Klass toote kohta, mis sisaldab poe nime, toote hinda, toote nime ja kilohinda.

### Klass Kasutaja
- **Eesmärk:** Klass kasutaja kohta, mis sisaldab ArrayListi soovitud toodetest ja poode, kus kasutaja käib.

## Projekti Tegemise Protsess
1. Mõtlesime välja ülesande struktuuri.
2. Kirjutasime vajalikud klassid ja meetodid.
3. Tegime veebikaabitsemise Coopi ja Rimi jaoks, kasutades JSOUPi.
4. Testisime loodud klasse ja nende kaabitsejaid.
5. Salvestasime tulemused listi ja sorteerisime tooted hinna alusel.
6. Rakendasime asünkroonsuse, et kiirendada protsessi.
7. Viimaks küsisime kasutajalt sisendit ja otsisime vastavate toodete kõige soodsamad variandid.

## Rühmaliikmete Panus ja Ajakulu
Kokku kulus aega umbes 10 tundi. Tööjaotus oli järgmine:
- Koos mõtlesime struktuuri ja dokumenteerisime.
- Andry tegi veebikaabitsemise Coopi ja Rimi jaoks, kasutades JSOUPi.
- Koos kirjutasime vajalikud meetodid ja klassid.
- Jan tegi kasutajaliidese ja asünkroonsuse.

## Tegemise Mured
Projekti kasutajasõbralikuks tööks oli tähtis see, et päringutele saadakse kiirelt vastused. Selle saavutamiseks pidime kasutama java asünkroonseid võimalusi. Tehisintellekti abiga saime selle tööle, kuid see võttis ikkagi aega ja katsetamist.
Suuremaks mureks kui asünkroonsus, osutus aga see, et osadel veebipoodidel ei ole aga hindasid/tooteid html lehel, seega saime esialgu tööle ainult Coopi ja Rimi. Näiteks Prisma e-pood näitab toote hinda vaid alles siis, kui oled selle oma korvi lisanud.

## Hinnang Lõpptulemusele
Oleme rahul oma tööga ning meile tundub, et sellest oleks kasu paljudele inimestele. Kui programmi viimistleda, rohkeim poode lisada ning teha ligipääsetavamaks, siis oleks see päris lahe projekt.

## Testimine
Peale iga suurema funktsionaalsuse rakendamist, katsetasime peaklassis, kas kõik osad koos töötavad enne, kui läksime edasi.
Nt. Tegime coopi kaabitseja -> proovisime kas saame tooted, kus nime sees on otsingi.
Tegime Rimi kaabitseja -> proovisime samamoodi.
Tulemused iga otsingu pealt koondasime ühte listi. Sorteerisime. Uurisime kas saime oodatud tulemuse.
Proovisime mitme otsingusõna pealt nt. Hapukurk, majonees… (iga otsingu pealt loodi eraldi ArrayList toodetest)
Rakendasime asünkroonsuse -> veendusime, et otsing läheb kiiremaks
