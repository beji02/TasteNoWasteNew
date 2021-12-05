// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getDatabase, ref, set , get, onValue} from "firebase/database";


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
    }
     
    async getLocationPackagesList () {
        const dbRef = ref(this.db, 'Users');
        
        onValue(dbRef, (snapshot) => {
          snapshot.forEach((childSnapshot) => {
            const childKey = childSnapshot.key;
            const childData = childSnapshot.val()
            const packageList = this.getPackagesOfUser(childKey)
            const promise1 = new Promise((resolve, reject) => {
                resolve('Success!');
              });
              promise1.then((packageList) => {
                console.log(packageList);
                // expected output: "Success!"
              });
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

    async populateDatabase (){
        this.writeUserData("Radu", "radu@yahoo.com", "0749159835", 45.754, 21.259, "Plastic")
        this.writeUserData("Radu Trial2", "radu@yahoo.com", "0749159835", 45.739, 21.1937, "Paper")
    }

    async writeUserData(name, email, phoneNumber, lat, long, itemPackage) {
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
        const randomStorageID = (Math.random() + 1).toString(36).substring(2);
        set(ref(this.db, 'Users/' + randomUserID + '/Storage/' + randomStorageID),{
            packages: itemPackage
         });
      }
}
