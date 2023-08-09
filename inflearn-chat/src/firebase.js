import {initializeApp} from "firebase/app";
import {getStorage, ref, uploadBytes, getDownloadURL} from "firebase/storage";
import {getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, updateProfile} from 'firebase/auth'
import config from '../firebase.json';

const app = initializeApp(config)
const auth = getAuth(app)
const storage = getStorage(app);

export const signIn = async ({email, password}) => {
  return await signInWithEmailAndPassword(auth, email, password)
}

const uploadImage = async uri => {
  if (uri.startsWith('https')) {
    return uri
  }

  const blob = await new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()
    xhr.onload = function () {
      resolve(xhr.response)
    }
    xhr.onerror = function () {
      reject(new TypeError('Network request failed'))
    }
    xhr.responseType = 'blob'
    xhr.open('GET', uri, true)
    xhr.send(null)
  })
  const user = auth.currentUser
  const storageRef = ref(storage,`profile/${user.uid}/photo.png`)
  const metadata = { contentType: 'image/png', }
  await uploadBytes(storageRef, blob, metadata)
  return await getDownloadURL(storageRef)
}

export const signUp = async ({name, email, password, photo}) => {
  const {user} = await createUserWithEmailAndPassword(auth, email, password)
  const photoURL = await uploadImage(photo)
  await updateProfile(user,{ displayName: name, photoURL: photoURL})
  return user
}
