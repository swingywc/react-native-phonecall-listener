import { NativeEventEmitter, NativeModules } from 'react-native';

const { RNPhonecallListener } = NativeModules;
const PhonecallListenerEmitter = new NativeEventEmitter(RNPhonecallListener || {});

const PhonecallListener = {
  EVENT_PHONECALL_RECEIVED: RNPhonecallListener.EVENT_PHONECALL_RECEIVED,
  addEventListener: (name, callback) => PhonecallListenerEmitter.addListener(name, callback),
  onCallReceived: (callback) => {
    return PhonecallListener.addEventListener(PhonecallListener.EVENT_PHONECALL_RECEIVED, callback)
  },
  enable: () => RNPhonecallListener.enable(),
  disable: () => RNPhonecallListener.disable(),
};

export default PhonecallListener;
