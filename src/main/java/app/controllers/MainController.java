package app.controllers;

import app.controllers.model.query.ScheduleController;
import app.controllers.model.query.VehicleController;
import app.views.ExtendedAnimatedFlowContainer;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

import java.io.IOException;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

@ViewController(value = "/fxml/main.fxml")
public class MainController {

    @FXMLViewFlowContext
    private ViewFlowContext mContext;

    @FXML
    private static StackPane mRoot;

    @FXML
    private StackPane mTitleBurgerContainer;

    @FXML
    private JFXHamburger mTitleBurger;

    @FXML
    private JFXDrawer mDrawer;

    public static StackPane getRoot() {
        return mRoot;
    }

    @PostConstruct
    public void init() throws IOException, FlowException {
        // Drawer
        mDrawer.setOnDrawerOpening(e -> {
            final Transition animation = mTitleBurger.getAnimation();
            animation.setRate(1);
            animation.play();
        });
        mDrawer.setOnDrawerClosing(e -> {
            final Transition animation = mTitleBurger.getAnimation();
            animation.setRate(-1);
            animation.play();
        });
        mTitleBurgerContainer.setOnMouseClicked(e -> {
            if (mDrawer.isHidden() || mDrawer.isHidding()) {
                mDrawer.open();
            } else {
                mDrawer.close();
            }
        });

        mContext = new ViewFlowContext();
        //  controller padrão (tela inicial)
        Flow innerFlow = new Flow(ScheduleController.class);
        final FlowHandler flowHandler = innerFlow.createHandler(mContext);
        mContext.register("ContentFlowHandler", flowHandler);
        mContext.register("ContentFlow", innerFlow);

        final Duration containerAnimationDuration = Duration.millis(200);
        mDrawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
        mContext.register("ContentPane", mDrawer.getContent().get(0));

        // Controller do menu lateral
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(mContext);
        mDrawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));

    }
}
