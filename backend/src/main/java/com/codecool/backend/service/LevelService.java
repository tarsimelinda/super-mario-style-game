package com.codecool.backend.service;

import com.codecool.backend.dto.CoinDto;
import com.codecool.backend.dto.EnemySpawnDto;
import com.codecool.backend.dto.LevelDto;
import com.codecool.backend.dto.PlatformDto;
import com.codecool.backend.dto.PointDto;
import com.codecool.backend.model.Enemy;
import com.codecool.backend.repository.EnemyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LevelService {

    private final PlatformGeneratorService platformGeneratorService;
    private final CoinGeneratorService coinGeneratorService;
    private final LevelValidatorService levelValidatorService;
    private final EnemyRepository enemyRepository;

    private final Random random = new Random();

    private static final int GROUND_Y = 550;
    private static final int PLAYER_HEIGHT = 50;

    private static final int ENEMY_WIDTH = 40;
    private static final int ENEMY_HEIGHT = 40;
    private static final int MIN_ENEMY_Y = 80;
    private static final int MAX_ENEMY_Y = 420;

    private static final PlatformDto RANDOM_LEVEL_START_PLATFORM =
            new PlatformDto(80, 460, 220, 20);

    private static final PointDto RANDOM_LEVEL_PLAYER_START =
            new PointDto(120, GROUND_Y - PLAYER_HEIGHT);

    private static final PointDto DEFAULT_LEVEL_PLAYER_START =
            new PointDto(150, GROUND_Y - PLAYER_HEIGHT);

    public LevelService(
            PlatformGeneratorService platformGeneratorService,
            CoinGeneratorService coinGeneratorService,
            LevelValidatorService levelValidatorService,
            EnemyRepository enemyRepository
    ) {
        this.platformGeneratorService = platformGeneratorService;
        this.coinGeneratorService = coinGeneratorService;
        this.levelValidatorService = levelValidatorService;
        this.enemyRepository = enemyRepository;
    }

    public LevelDto getDefaultLevel() {
        List<PlatformDto> platforms = getDefaultPlatforms();

        return new LevelDto(
                DEFAULT_LEVEL_PLAYER_START,
                3,
                platforms,
                getDefaultEnemies(),
                getDefaultCoins()
        );
    }

    public LevelDto getRandomLevel() {
        for (int attempt = 0; attempt < 50; attempt++) {
            List<PlatformDto> platforms = platformGeneratorService.generatePlatforms();

            if (!levelValidatorService.isPlayable(RANDOM_LEVEL_START_PLATFORM, platforms)) {
                continue;
            }

            List<CoinDto> coins = coinGeneratorService.generateCoins(platforms);
            List<EnemySpawnDto> enemies = generateEnemies(platforms);

            return new LevelDto(
                    RANDOM_LEVEL_PLAYER_START,
                    3,
                    platforms,
                    enemies,
                    coins
            );
        }

        return getDefaultLevel();
    }

    private List<PlatformDto> getDefaultPlatforms() {
        return List.of(
                new PlatformDto(100, 450, 200, 20),
                new PlatformDto(400, 380, 150, 20),
                new PlatformDto(650, 320, 200, 20),
                new PlatformDto(950, 250, 150, 20),
                new PlatformDto(1250, 200, 50, 20),
                new PlatformDto(650, 150, 200, 20),
                new PlatformDto(400, 100, 100, 20),
                new PlatformDto(100, 150, 70, 20)
        );
    }

    private List<EnemySpawnDto> getDefaultEnemies() {
        return List.of(
                new EnemySpawnDto(500, 310, 40, 40, 2, 1, 1, "blue", false),
                new EnemySpawnDto(300, 160, 40, 40, 3, 2, 1, "black", false)
        );
    }

    private List<CoinDto> getDefaultCoins() {
        return List.of(
                new CoinDto(150, 410, 20, 20),
                new CoinDto(230, 410, 20, 20),

                new CoinDto(450, 340, 20, 20),
                new CoinDto(510, 340, 20, 20),

                new CoinDto(710, 280, 20, 20),
                new CoinDto(790, 280, 20, 20),

                new CoinDto(1000, 210, 20, 20),

                new CoinDto(700, 110, 20, 20),
                new CoinDto(760, 110, 20, 20),

                new CoinDto(130, 110, 20, 20)
        );
    }

    private List<EnemySpawnDto> generateEnemies(List<PlatformDto> platforms) {
        List<Enemy> enemyTypes = getEnemyTypes();

        return platforms.stream()
                .skip(1)
                .filter(platform -> platform.width() >= 90)
                .filter(this::isSafeEnemyPlatform)
                .filter(platform -> random.nextInt(100) < 35)
                .limit(calculateEnemyCount(platforms))
                .map(platform -> {
                    Enemy enemyType = enemyTypes.get(random.nextInt(enemyTypes.size()));

                    return new EnemySpawnDto(
                            platform.x() + platform.width() / 2 - ENEMY_WIDTH / 2,
                            platform.y() - ENEMY_HEIGHT,
                            ENEMY_WIDTH,
                            ENEMY_HEIGHT,
                            enemyType.getSpeed(),
                            enemyType.getDamage(),
                            enemyType.getHp(),
                            enemyType.getColor(),
                            enemyType.isCanJump()
                    );
                })
                .toList();
    }

    private List<Enemy> getEnemyTypes() {
        List<Enemy> enemyTypes = enemyRepository.findAll();

        if (!enemyTypes.isEmpty()) {
            return enemyTypes;
        }

        return getFallbackEnemyTypes();
    }

    private List<Enemy> getFallbackEnemyTypes() {
        return List.of(
                new Enemy(null, 1, 2, 1, "blue", false),
                new Enemy(null, 1, 3, 1, "purple", true),
                new Enemy(null, 2, 2, 2, "black", false),
                new Enemy(null, 1, 1, 3, "darkred", false),
                new Enemy(null, 1, 3, 2, "orange", true)
        );
    }

    private boolean isSafeEnemyPlatform(PlatformDto platform) {
        int enemyY = platform.y() - ENEMY_HEIGHT;

        return enemyY >= MIN_ENEMY_Y && enemyY <= MAX_ENEMY_Y;
    }

    private int calculateEnemyCount(List<PlatformDto> platforms) {
        return Math.max(1, Math.min(5, platforms.size() / 6));
    }
}