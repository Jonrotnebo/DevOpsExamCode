#Card Application

##Hva Applikasjonen inneholder
- Metrics og en influx db.
- LogzIo konfigurasjon.
- Ett enkelt restApi med enkle tester.
- En kortfattet oppsett på hvordan sette opp gcp nøkkel fil fra kommandolinje
- Kort forklaring om valg og hva som ikke er lagt ved.

##Hvordan installere og sette hemligheter i kommandolinje.

### 1. Installer gcloud og sett dette opp.

### Step 2:

```"PROJECT_ID="$(gcloud config get-value project -q)"```
 
 Bruk eventuelt:
 
```Gcloud config set project <project-id>```

Dette vil gjøre det manuelt om en ikke finner riktig prosjekt fra kommandolinje, for å se project id bruk:

```Gcloud project list```

Bruk så: dette velger navn.

```SVCACCT_NAME=travisci-deployer```

###Lage en service account fra Kommandolinje:
- En kan også lage dette rett fra google cloud platform.

- ```gcloud iam service-accounts create "${SVCACCT_NAME?}"```

#####Finn epost adressen:

```
SVCACCT_EMAIL="$(gcloud iam service-accounts list \
--filter="name:${SVCACCT_NAME?}@"  \
--format=value\(email\))"
```

#####Lage JSON fil, DENNE SKAL IKKE LEGGES UT PÅ GIT, LAGRE DENNE SIKKERT!.
```
gcloud iam service-accounts keys create "google-key.json" \
   --iam-account="${SVCACCT_EMAIL?}"
```

#####Legg til tilatelser/permissions. Sørg for å ha storage, cloudrun og cloudbuild aktivert i GCP.
#####Storage admin: brukes for å pushe docker images.
```
-	gcloud projects add-iam-policy-binding "${PROJECT_ID?}" \
-	   --member="serviceAccount:${SVCACCT_EMAIL?}" \
-   --role="roles/storage.admin"
```

#####Cloud Run Admin Brukes for å deployere til Cloud Run:
```
gcloud projects add-iam-policy-binding "${PROJECT_ID?}" \
   --member="serviceAccount:${SVCACCT_EMAIL?}" \
   --role="roles/run.admin"
```

#####IAm service account User: Nødvendig for gcloud run å “late som” den er runtime identiteten.
```
gcloud projects add-iam-policy-binding "${PROJECT_ID?}" \
   --member="serviceAccount:${SVCACCT_EMAIL?}" \
   --role="roles/iam.serviceAccountUser"
```

- Encrypt travis filen med:
```travis encrypt-file <Legg inn google-file-navnet her>.json -–add```
- add her legger filen til automatisk.

Endre .travis.yml under enviorment som: ```GCP_PROJCET_ID, IMAGE, CLOUD RUN SERVICE```

Push til git og se bygget fullføre.


###Valg i applikasjonen og innhold.
- Metrics i applikasjonen benytter seg kun av Timer og Counter.
    - Timer Viser tiden det tar å gjennomføre en create eller get av dataen.
- Influxdb konfigurasjonen ligger ikke vedlagt vedlagt, men kan enkelt konfigureres etter eget behov.
    - For å sette opp en egen influx db kan en kjøre følgende:
    - ``docker run --rm influxdb:1.0 influxd config > influxdb.conf ``
    - For å starte influx kan en også kjøre: 
    - ``` docker run --name influxdb \
        -p 8083:8083 -p 8086:8086 -p 25826:25826/udp \
        -v $PWD/influxdb:/var/lib/influxdb \
        -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
        -v $PWD/types.db:/usr/share/collectd/types.db:ro \
        influxdb:1.0
      ```
    - Dette ligger ikke vedlagt da oppgave teksten sier config filer ikke skal deles, og en antar dette skal kunne 
    kjøres lokalt på egen maskin.
        - Dette tilsier att konfigurasjonen av influx må en selv gjøre ved å lage en egen mydb.
        - Det betyr også en må hente dataen selv fra grafana, og konfigurere denne.
      
- Dataen benytter seg av en h2 database, den bruker ikke Postgres eller liknende da 
dette ikke er nævendig for en så liten applikasjon.
- Dataen fra metrics blir sendt ut til logz io, men kan også konfigureres lokalt på maskinen med grafana.
    - For å kjøre dette lokalt, uten om localApplicationRunner må en sette inn egne verdier for logz-token og url,
     se infrastructur repo for hvordan dette kan gjøres.
- Logz Io kompatiblitet er vedlagt, denne må bruker konfigurere selv, se Infrastruktur for hvordan dette gjøres.
    - Applikasjonen mangler LongTaskTimer fra microregistry og gauge.

