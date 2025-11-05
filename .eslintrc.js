module.exports = {
    env: {
        browser: true,
        es2021: true,
        node: true,
    },
    extends: ["eslint:recommended", "plugin:react/recommended", "plugin:react-hooks/recommended"],
    parserOptions: {
        ecmaFeatures: { jsx: true },
        ecmaVersion: 12,
        sourceType: "module",
    },
    plugins: ["react"],
    rules: {
        "react/prop-types": "off", // disable if using TypeScript or you prefer not to use prop-types
        "no-console": "warn",
        "prefer-const": "error",
    },
    settings: { react: { version: "detect" } },
};
