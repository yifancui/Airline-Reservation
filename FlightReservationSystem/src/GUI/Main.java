package GUI;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{
	

	@Override
	public void start(Stage stage) throws Exception {
		StateMachine sm = StateMachine.getInstance();
		sm.sceneSwitcher = new SceneSwitcher(stage);
		sm.switchState(StateMachine.state.input_params_first);
		stage.show();
    }

    public static void main(String[] args) {
        launch(args);
	}

}
