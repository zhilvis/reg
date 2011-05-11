package lt.reserv;

import lt.reserv.register.RegistrationPage;

import org.apache.wicket.IPageManagerProvider;
import org.apache.wicket.Page;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.page.IPageManagerContext;
import org.apache.wicket.page.PersistentPageManager;
import org.apache.wicket.pageStore.DefaultPageStore;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.IPageStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.WebApplication;

public class ReservApp extends WebApplication{
	
	@Override
	protected void init() {
		setPageManagerProvider(new IPageManagerProvider() {
			@Override
			public IPageManager get(IPageManagerContext context) {
				
				IDataStore dataStore = new HttpSessionDataStore(context, 
						new PageNumberEvictionStrategy(15));
					
				IPageStore pageStore = new DefaultPageStore(ReservApp.this.getName(), dataStore, 40);
				return new PersistentPageManager(ReservApp.this.getName(), pageStore, context);
			}
		});
		
		super.init();
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return RegistrationPage.class;
	}
}
