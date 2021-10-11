package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.utils.Record;
import com.oscarjco.fallblocks.utils.RecordsList;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

public class RecordsScreen extends BaseScreen {
    Table recordsTable;
    private final int MARATHON_MODE = 0;
    private final int TEN_MINUTES_MODE = 1;
    private final int FORTY_LINES_MODE = 2;
    int mode;

    public RecordsScreen(com.oscarjco.fallblocks.MainGame game) {
        super(game);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    parent.changeScreen(com.oscarjco.fallblocks.MainGame.MENU);
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();

        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        mode = MARATHON_MODE;

        Label title = new Label(i18n.get("menu.records"), skin);
        title.setFontScale(4);
        title.setPosition(
                WIDTH/2 - title.getWidth()*2,
                HEIGHT - title.getHeight()*6
        );
        title.setColor(Color.GREEN);
        stage.addActor(title);

        Label modeLabel = new Label(i18n.get("records.mode") + ":", skin);
        modeLabel.setFontScale(4);
        modeLabel.setPosition(
                WIDTH / 8,
                HEIGHT * 0.8f
        );
        stage.addActor(modeLabel);

        final SelectBox<String> selectBox = new SelectBox<String>(skin);
        selectBox.setItems(i18n.get("modes.marathon"), i18n.get("modes.tenMinutes"), i18n.get("modes.fortyLines"));
        selectBox.getStyle().listStyle.font.getData().scale(3);
        selectBox.setBounds(
                WIDTH / 2,
                HEIGHT * 0.8f - selectBox.getHeight(),
                WIDTH * 0.45f,
                120
        );
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mode = selectBox.getSelectedIndex();
                createRecordsTable();
            }
        });
        stage.addActor(selectBox);

        createRecordsTable();

        TextButton deleteRecords = new TextButton(i18n.get("buttons.deleteRec"), skin);
        deleteRecords.getLabel().setWrap(true);
        deleteRecords.setBounds(
                WIDTH * 0.55f, 80,
                WIDTH / 2.5f, 180
        );
        deleteRecords.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                String file = "";

                if(mode == MARATHON_MODE) {
                    file = "marathon.xml";
                } else if(mode == TEN_MINUTES_MODE) {
                    file = "ten_minutes.xml";
                } else if(mode == FORTY_LINES_MODE) {
                    file = "forty_lines.xml";
                }

                if(!file.equals("")) {
                    return Gdx.files.local(file).delete();
                }

                return false;
            }
        });

        TextButton back = new TextButton(i18n.get("buttons.back"), skin);
        back.setPosition(WIDTH * 0.05f, 80);
        back.setSize(WIDTH / 2.5f, 180);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                com.oscarjco.fallblocks.utils.GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.MENU);
            }
        });

        stage.addActor(back);
        stage.addActor(deleteRecords);
        stage.addActor(recordsTable);
    }

    @Override
    public void hide() {
        super.hide();
        recordsTable.clear();
    }

    private void createRecordsTable() {
        for (Actor actor : stage.getActors()) {
            if(actor.equals(recordsTable)) {
                recordsTable.clear();
            }
        }

        if(recordsTable == null) {
            recordsTable = new Table();
            recordsTable.setSkin(skin);
            recordsTable.setFillParent(true);
            recordsTable.setY(-80);
        }

        String gameMode = "";

        switch (mode) {
            case MARATHON_MODE:
                gameMode = "marathon";
                break;
            case TEN_MINUTES_MODE:
                gameMode = "ten_minutes";
                break;
            case FORTY_LINES_MODE:
                gameMode = "forty_lines";
                break;
        }

        com.oscarjco.fallblocks.utils.RecordsList records = new RecordsList(gameMode);
        recordsTable.add(i18n.get("records.name"), "font", Color.LIGHT_GRAY).expandX().uniformX();
        if(mode == FORTY_LINES_MODE) {
            recordsTable.add(i18n.get("modes.time"), "font", Color.LIGHT_GRAY).expandX().uniformX();
        } else {
            recordsTable.add(i18n.get("modes.score"), "font", Color.LIGHT_GRAY).expandX().uniformX();
        }
        recordsTable.add(i18n.get("records.date"), "font", Color.LIGHT_GRAY).expandX().uniformX();

        int i = 0;
        Vector<com.oscarjco.fallblocks.utils.Record> recordsList = records.getRecordsList();
        Comparator<com.oscarjco.fallblocks.utils.Record> comparator = new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                if(mode == FORTY_LINES_MODE) {
                    return (int) (((com.oscarjco.fallblocks.utils.Record)o).getScore() - ((com.oscarjco.fallblocks.utils.Record)t1).getScore());
                } else {
                    return (int) (((com.oscarjco.fallblocks.utils.Record)t1).getScore() - ((com.oscarjco.fallblocks.utils.Record)o).getScore());
                }
            }
        };
        Collections.sort(recordsList, comparator);

        for(Record rec : records.getRecordsList()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            String formattedDate = formatter.format(rec.getDate());

            Label name = new Label(rec.getName(), skin);
            name.setWrap(true);
            name.setAlignment(Align.center);
            name.setFontScale(3.5f);
            name.setColor(Color.GREEN);
            Label score = new Label("", skin);

            if(mode == FORTY_LINES_MODE) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                format.getTimeZone().setRawOffset(0);
                score.setText(format.format(rec.getScore()));
            } else {
                score.setText(String.valueOf(rec.getScore()));
            }

            score.setWrap(true);
            score.setFontScale(3.5f);
            score.setAlignment(Align.center);
            Label date = new Label(formattedDate, skin);
            date.setWrap(true);
            date.setFontScale(3.5f);
            date.setAlignment(Align.center);

            recordsTable.row().padTop(16);
            recordsTable.add(name).fillX().uniformX();
            recordsTable.add(score).fillX().expandX().uniformX();
            recordsTable.add(date).fillX().expandX().uniformX();
            i++;
        }

        while (i < 10) {
            recordsTable.row().padTop(16);
            recordsTable.add("---", "font", Color.WHITE).uniformX();
            recordsTable.add("---", "font", Color.WHITE).uniformX();
            recordsTable.add("---", "font", Color.WHITE).uniformX();
            i++;
        }

    }
}
