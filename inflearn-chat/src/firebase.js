import {initializeApp} from "firebase/app";
import {getStorage, ref, uploadBytes, getDownloadURL} from "firebase/storage";
import {getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, updateProfile, signOut} from 'firebase/auth'
import {getFirestore, doc, setDoc} from "firebase/firestore"
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
  const storageRef = ref(storage, `profile/${user.uid}/photo.png`)
  const metadata = {contentType: 'image/png',}
  await uploadBytes(storageRef, blob, metadata)
  return await getDownloadURL(storageRef)
}

export const signUp = async ({name, email, password, photo}) => {
  const {user} = await createUserWithEmailAndPassword(auth, email, password)
  const photoURL = await uploadImage(photo)
  await updateProfile(user, {displayName: name, photoURL: photoURL})
  return user
}

export const getCurrentUser = () => {
  const {uid, displayName, email, photoURL} = auth.currentUser
  return {uid, name: displayName, email, photo: photoURL}
}

export const updateUserInfo = async photo => {
  const photoURL = await uploadImage(photo)
  const user = auth.currentUser;
  await updateProfile(user, {photoURL})
  return photoURL
}

export const signout = async () => {
  await signOut(auth)
  return {}
}

const db = getFirestore(app)

export const createChannel = async ({title, desc}) => {
  const newChannelRef = doc(db,'channels', title)
  const id = newChannelRef.id
  const newChannel = {
    id,
    title,
    description: desc,
    create: Date.now(),
  }
  await setDoc(newChannelRef, newChannel);
  return id;
}
