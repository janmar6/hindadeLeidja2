## Autorid
Nimed: Jan Markus Salum, Andry Avamägi

## Projekti Põhjalik Kirjeldus

Tegemist on sama projektiga, mis rühmatöö 1 valmistasime, aga lisasime graafilise liidese, faili kirjutamise ja Prisma e-poest lugemise.
Programmi eesmärk on otsida ja kuvada kasutajale kõige odavamad versioonid toodetest, mida ta soovib. Programm otsib paralleelselt st mitmelõimeliselt COOP, Rimi ja Prisma e-poodidest kõik sobivad tooted, sorteerib ja kuvab kasutajale kolmest poest kõige odavama toote - nii iga toote kohta.


Kasutaja käivitab HelloApplication.java ja hakkab avanenud aknas otsinguribale tooteid sisestama (nt. Kurk, piim, juust jne..) ning iga Enter klahvi vajutusega lisatakse see toode listi. Kui kasutaja on oma listiga rahul siis ta saab vajutada search, mille peale hakkab programm tooteid otsima.
Tulemuseks kuvatakse aknasse kõik kõige odavamad tooted ning kui kasutaja vajutab toote peale, siis kuvatakse kõrvalaknas sellest tootest kõik võimalikud variandid.

## Klassid

### HelloApplication 
- **Eesmärk:** Peaklass, mis jooksutab ja tegeleb JavaFX programmiga.

### Abstraktne Klass Kaabitseja
- **Eesmärk:** Abstraktne klass, mida coop ja rimi klass extendivad ja mis sisaldab funktsiooni `kaabitse`, mis tagastab ArrayListi toodetest vastavalt otsingusõnele.

### Klass COOP
- **Eesmärk:** Extendib Kaabitsejat, kaabitsedes COOPi veebipoest.

### Klass Rimi
- **Eesmärk:** Extendib Kaabitsejat, kaabitsedes Rimi veebipoest.

### Klass Prisma
- **Eesmärk:** Extendib Kaabitsejat, kaabitsedes Rimi veebipoest.

### Klass Toode
- **Eesmärk:** Klass toote kohta, mis sisaldab poe nime, toote hinda, toote nime ja kilohinda.

### Klass Kasutaja
- **Eesmärk:** Klass kasutaja kohta, mis sisaldab ArrayListi soovitud toodetest ja poode, kus kasutaja käib.### Klass Kasutaja

### meetodite kohta info klasside sees kommentaaride kujul

## Projekti Tegemise Protsess, Rühmaliikmete Panus ja Ajakulu

### Tegemist eelmise projekti jätkuga. Alguse kohta lugeda eelmise projekti readme failist.
Jan Markus Salum: 
Selles etapis oli minu ülesandeks teha kasutajaliides toodete otsimiseks, kuvamiseks, faili salvestamiseks. huvitavaim osa minu jaoks oli lisa paneel, mis sarnaste toodete hindu näitab (ja see kuidas see avaneb jne). Kokku kulus umbes 8h.

Andry Avamägi:
Selles etapis oli minu ülesandeks saada otsingutulemused prisma e-poest. Esimesel korral see meil ei õnnestunud, kuna iga päringuga tagastati meile javascript - sealt oli väga raske / võimatu midagi välja lugeda. Minu ülesandeks oli välja mõelda viis, kuidas see javascript tagaplaanil ära renderida ja tulemusest info välja lugeda. Leidsin sellise teegi nagu Selenium, mis võimaldas täpselt seda - tegemist vist eelkõige veebitestide jaoks kirjutatud teegiga, kuid see sobis minu ülesande jaoks. Tulemusena sain valmis programmi, mis iga funktsiooni välja kutsega avab tagaplaanil chromekliendi, kust kutsutakse välja sobiv päring - nüüd see klient käitub brauserina ja renderdab javascripti ning selle tulemusest  oli väga lihtne otsida välja kõik vajalik info ja salvestada see Toodete klassidena. Kokku kulus mul umbes 6h, kuna Seleniumi rakendamisel ilmnesid errori, millest algul midagi aru ei saanud. 



## Tegemise Mured
Projekti kasutajasõbralikuks tööks oli tähtis see, et päringutele saadakse kiirelt vastused. Selle saavutamiseks pidime kasutama java asünkroonseid võimalusi. Tehisintellekti abiga saime selle tööle, kuid see võttis ikkagi aega ja katsetamist.
Suuremaks mureks kui asünkroonsus, osutus aga see, et osadel veebipoodidel ei ole aga hindasid/tooteid html lehel, seega saime esialgu tööle ainult Coopi ja Rimi. Näiteks Prisma e-pood näitab toote hinda vaid alles siis, kui oled selle oma korvi lisanud.

## Hinnang Lõpptulemusele
Arvame, et lõpuks sai valmis väga äge projekt ning oleme oma tulemusega rahul.

## Testimine
Peale iga suurema funktsionaalsuse rakendamist, katsetasime klasse, kas kõik osad koos töötavad enne, kui läksime edasi.
Nt. Tegime coopi kaabitseja -> proovisime kas saame tooted, kus nime sees on otsing. Prisma ja Rimiga samamoodi.
Tulemused iga otsingu pealt koondasime ühte listi. Sorteerisime. Uurisime kas saime oodatud tulemuse.
Proovisime erinevaid tektse panna tektsivälja, erinavaid nupukombinatsioone ning andsime teistele inimestele testimiseks ka.