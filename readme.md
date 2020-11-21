#Card Application
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
