package app.controllers;

import app.controllers.model.query.ScheduleController;
import app.views.ExtendedAnimatedFlowContainer;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private static JFXDrawer mDrawer;

    @FXML
    private static Label mTitle;

    public static StackPane getRoot() {
        return mRoot;
    }

    public static void closeDrawer() {
        final KeyFrame keyFrame = new KeyFrame(Duration.millis(50), e -> mDrawer.close());
        final Timeline timeline = new Timeline(keyFrame);
        Platform.runLater(timeline::play);
    }

    public static void setTitle(String title) {
        mTitle.setText(title);
    }

    @PostConstruct
    public void init() throws IOException, FlowException {
        // Drawer
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
