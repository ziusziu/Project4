package app.ga.com.headingout.inputfragment;

import javax.inject.Singleton;

import app.ga.com.headingout.inputfragment.tabfragment.InputHotelTabFragment;
import dagger.Component;

/**
 * Created by samsiu on 6/10/16.
 */
@Singleton
@Component(modules={NetModule.class})
public interface NetComponent {

    void inject(InputHotelTabFragment fragment);

}
