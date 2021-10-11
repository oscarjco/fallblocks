package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.utils.RecordsList;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.Record;

import java.util.Date;
import java.util.Vector;

public class EndScreen extends BaseScreen {
    Texture img;
    long score;
    boolean isNewRecord;
    boolean cleared;
    String mode;
    float imgY;

    public EndScreen(MainGame game, String mode, long score, boolean cleared){
        super(game);
        this.score = score;
        this.mode = mode;
        this.cleared = cleared;

        if(cleared) {
            img = new Texture(Gdx.files.internal("imgs/cleared.png"));
        } else {
            img = new Texture(Gdx.files.internal("imgs/game_over.png"));
        }

        imgY = HEIGHT * 0.9f;
    }

    @Override
    public void show() {
        super.show();

        GameAudio.stop();

        stage.addAction(Actions.alpha(0));
        stage.act(0);
        stage.addAction(Actions.delay(1, Actions.fadeIn(1)));

        if(cleared) {
            final RecordsList records = new RecordsList(mode);
            Vector<Record> recordList = records.getRecordsList();

            if(recordList.isEmpty() || recordList.size() < 10) {
                isNewRecord = true;
            } else {
                for(Record rec : records.getRecordsList()) {
                    if(score > rec.getScore()) {
                        isNewRecord = true;
                        break;
                    }
                }
            }

            if(isNewRecord) {
                GameAudio.playSound(GameAudio.NEW_RECORD);

                final Dialog nameDialog = new Dialog("", skin);
                nameDialog.setBounds(
                        WIDTH * 0.125f, HEIGHT * 0.4f,
                        WIDTH * 0.75f, HEIGHT * 0.25f);

                Label label = new Label(i18n.get("clear.record"), skin);
                final TextField textField = new TextField("", skin);
                TextButton ok = new TextButton(i18n.get("buttons.confirm"), skin);
                TextButton skip = new TextButton(i18n.get("buttons.skip"), skin);
                nameDialog.getContentTable().add(label).expandX().center();
                nameDialog.getContentTable().row().padTop(40);
                textField.setMaxLength(16);
                textField.getStyle().font.getData().setScale(3);
                textField.setAlignment(Align.center);
                textField.setMessageText(i18n.get("clear.inputName"));
                nameDialog.getContentTable().add(textField).expandX().width(WIDTH/2).center();
                ok.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GameAudio.playSound(GameAudio.BUTTON_CLICK);

                        String name = textField.getText();
                        if(!name.equals("")) {
                            records.saveScore(name, score, new Date());
                            nameDialog.clear();
                            showDialog();
                        } else {
                            textField.setMessageText(i18n.get("clear.error"));
                        }
                    }
                });
                skip.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GameAudio.playSound(GameAudio.BUTTON_CLICK);

                        nameDialog.clear();
                        showDialog();
                    }
                });

                nameDialog.getButtonTable().add(ok).expandX().fillX();
                nameDialog.getButtonTable().add(skip).expandX().fillX();
                stage.addActor(nameDialog);
            } else {
                GameAudio.playSound(GameAudio.CLEAR);
                showDialog();
            }
        } else {
            showDialog();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();

        if(imgY > HEIGHT * 0.8f - img.getHeight()) {
            imgY -= 8;
        }

        batch.draw(img,
                WIDTH / 4, imgY,
                WIDTH / 2, HEIGHT / 8);
        batch.end();
    }

    public void showDialog() {
        Dialog dialog = new Dialog("", skin);
        dialog.setBounds(
                WIDTH * 0.125f, HEIGHT * 0.4f,
                WIDTH * 0.75f, HEIGHT * 0.25f);
        dialog.getContentTable().add(i18n.get("clear.playAgain"), "font", Color.WHITE);
        TextButton yes = new TextButton(i18n.get("buttons.yes"), skin);
        TextButton no = new TextButton(i18n.get("buttons.no"), skin);
        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.currentMode);
            }
        });
        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.MENU);
            }
        });
        dialog.getButtonTable().add(yes).expandX().fillX();
        dialog.getButtonTable().add(no).expandX().fillX();
        stage.addActor(dialog);
    }

}
