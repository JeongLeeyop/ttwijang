module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/essential',
    '@vue/airbnb',
    '@vue/typescript/recommended',
  ],
  parserOptions: {
    ecmaVersion: 2020,
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-param-reassign': 'off',
    'linebreak-style': 0,
    'linebreak-style': 0,
    'max-len': 'off',
    'no-underscore-dangle': 0,
    'class-methods-use-this': 'off',
    'import/no-unresolved': 'off',
    'import/no-cycle': 'off',
    'import/prefer-default-export': 'off',
    'import/extensions': 'off',
    'arrow-body-style': 'off',
    'prefer-destructuring': 'off',
    'arrow-body-style': 'off',
    'new-cap': 'off',
		'no-tabs': 'off',
		indent: 'off',
    '@typescript-eslint/interface-name-prefix': 'off',
    '@typescript-eslint/ban-types': 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    '@typescript-eslint/member-delimiter-style': ['error',
      {
        multiline: {
          delimiter: 'none',
        },
        singleline: {
          delimiter: 'comma',
        },
      }],
    '@typescript-eslint/no-explicit-any': 'off',
  },
};
