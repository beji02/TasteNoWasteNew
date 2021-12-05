// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getDatabase, ref, set , get, onValue} from "firebase/database";

const bottomLeftCoord = {latitude: 45.7188, longitute: 21.1956}
const topRightCoord = {latitude: 45.7856 , longitute: 21.2800}

const packagesTypes = ["Paper", "Plastic", "Rest"]


const firebaseConfig = {
  apiKey: "AIzaSyBgPOalmsoA8qXh9hc9pC1trlJUnXOkiI8",
  authDomain: "tastenowaste-59b51.firebaseapp.com",
  databaseURL: "https://tastenowaste-59b51-default-rtdb.europe-west1.firebasedatabase.app",
  projectId: "tastenowaste-59b51",
  storageBucket: "tastenowaste-59b51.appspot.com",
  messagingSenderId: "504907082584",
  appId: "1:504907082584:web:9ca9a7c92c3308e955d32d",
  measurementId: "G-2E0KXJ6SKW"
};

const app = initializeApp(firebaseConfig);

const userId = "RwRG9yhILwRgWoGTfQGuur4eNVl1"

export default class DataReader {
    constructor(props) {
        this.locationPackagesList = []

        this.db = getDatabase();

        this.bruteLocationPackageList = []
    }
     
    async getLocationPackagesList () {
        const dbRef = ref(this.db, 'Users');
        
        onValue(dbRef, (snapshot) => {
          snapshot.forEach((childSnapshot) => {
            const childKey = childSnapshot.key;
            const childData = childSnapshot.val()
            const packageList = this.getPackagesOfUser(childKey)
            this.locationPackagesList.push({
                location: 1,
                packages: packageList
            })
          });
          
        }, {
          onlyOnce: true
        });
        return this.locationPackagesList
    }

    getBruteLocationPackagesList () {
      return this.bruteLocationPackageList
    }

    async getPackagesOfUser (userKey) {
        const dbRef = ref(this.db, 'Users/' + userKey + '/Storage');
        
        let packagesOfUser = []

        onValue(dbRef, (snapshot) => {
          snapshot.forEach((childSnapshot) => {
            const childKey = childSnapshot.key
            const childData = childSnapshot.val()
            packagesOfUser.push(childData.packages)
          });
        }, {
          onlyOnce: true
        });

        return packagesOfUser
    }

    sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    populateDatabase (){
        const latitidudeStepSize = 0.0035
        const longitudeStepSize = 0.01

        for (let latitudeCoord = bottomLeftCoord.latitude; latitudeCoord < topRightCoord.latitude; latitudeCoord += latitidudeStepSize) {
          for (let longitudeCoord = bottomLeftCoord.longitute; longitudeCoord < topRightCoord.longitute; longitudeCoord += longitudeStepSize) {
              const name = "UserAt" + latitudeCoord + longitudeCoord
              const email = "EmailAt" + latitudeCoord + longitudeCoord
              
              let itemPackages = []
              for (let i=0; i<3; ++i) {
                const randomPackageIndex = Math.floor(Math.random() * 10) % 3
                itemPackages.push(packagesTypes[randomPackageIndex])
              }
              
              this.bruteLocationPackageList.push({
                coordinates: {
                  latitude: latitudeCoord,
                  longitude: longitudeCoord
                },
                packages: itemPackages
                })

              /*console.log(name)
              console.log(email)
              console.log(latitudeCoord + " --- " + longitudeCoord)
              console.log(itemPackages)*/
          }
        }
    }

    async writeUserData(name, email, phoneNumber, lat, long, itemPackages) {
        const randomUserID = (Math.random() + 1).toString(36).substring(2);
        set(ref(this.db, 'Users/' + randomUserID),{
           email: email,
           name: name,
           phoneNumber : phoneNumber,
           location: {
                latitude: lat,
                longitute: long
           }
        });

        for (let i=0; i<3; ++i){
            const randomStorageID = (Math.random() + 1).toString(36).substring(2);
            set(ref(this.db, 'Users/' + randomUserID + '/Storage/' + randomStorageID),{
                packages: itemPackages[i],
                name: "Product"
            });
        }
      }
}
