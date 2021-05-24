// MyEventBusService.aidl
package x.com.hl_study.Hermes.annotion;

// Declare any non-default types here with import statements
import x.com.hl_study.Hermes.annotion.Request;
import x.com.hl_study.Hermes.annotion.Responce;
interface MyEventBusService {
    Responce send(in Request request);
}
